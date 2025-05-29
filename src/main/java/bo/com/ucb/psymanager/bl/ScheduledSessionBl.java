package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.ScheduledSessionDao;
import bo.com.ucb.psymanager.dao.TherapistScheduledDao;
import bo.com.ucb.psymanager.dao.UserPatientDao;
import bo.com.ucb.psymanager.dao.UserTherapistDao;
import bo.com.ucb.psymanager.dto.UpcomingAppointmentDto;
import bo.com.ucb.psymanager.entities.*;
import bo.com.ucb.psymanager.events.AmqpEventPublisher;
import bo.com.ucb.psymanager.events.AppointmentBookedEvent;
import bo.com.ucb.psymanager.events.AppointmentRejectedEvent;
import bo.com.ucb.psymanager.events.AppointmentReminderEvent;
import bo.com.ucb.psymanager.exceptions.ScheduleNotFoundException;
import bo.com.ucb.psymanager.exceptions.SessionAlreadyExistsException;
import bo.com.ucb.psymanager.exceptions.UserNotPatientException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Lógica de negocio para la gestión de sesiones programadas (solicitud, aprobación y notificación).
 */
@Service
@RequiredArgsConstructor
public class ScheduledSessionBl {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledSessionBl.class);

    private final ScheduledSessionDao scheduledSessionDao;
    private final TherapistScheduledDao therapistScheduledDao;
    private final UserPatientDao userPatientDao;
    private final AmqpEventPublisher eventPublisher;
    private final UserTherapistDao userTherapistDao;

    /**
     * Registra una nueva sesión solicitada por un paciente para un horario disponible.
     * La sesión se guarda con estado PENDING.
     *
     * @param therapistScheduledId ID del horario
     * @param userId ID del paciente (desde token)
     */
    public void createScheduledSession(Long therapistScheduledId, Long userId, String reason)
    {
        UserPatient userPatient = userPatientDao.findById(userId)
                .orElseThrow(() -> new UserNotPatientException("El usuario no está registrado como paciente"));

        if (userPatient.getBlockedUntil() != null && userPatient.getBlockedUntil().isAfter(LocalDateTime.now())) {
            String unblockTime = userPatient.getBlockedUntil().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            throw new IllegalStateException("Tu cuenta está temporalmente bloqueada para solicitar citas. Podrás hacerlo nuevamente después del: " + unblockTime);
        }


        if (reason != null && reason.length() > 500) {
            throw new IllegalArgumentException("El motivo de la solicitud no debe exceder los 500 caracteres.");
        }


        String fullName = userPatient.getUser().getFirstName() + " " + userPatient.getUser().getLastName();
        logger.info("Paciente {} solicita reservar el horario {}", fullName, therapistScheduledId);

        TherapistScheduled schedule = therapistScheduledDao.findById(therapistScheduledId)
                .orElseThrow(() -> new ScheduleNotFoundException("El horario no existe"));

        boolean isTaken = scheduledSessionDao.existsByTherapistScheduled_TherapistScheduledIdAndStateIn(
                therapistScheduledId, List.of(SessionState.PENDING, SessionState.ACCEPTED)
        );
        if (isTaken) {
            throw new SessionAlreadyExistsException("Este horario ya fue reservado o está pendiente de confirmación");
        }

        boolean hasPending = scheduledSessionDao.existsByUserPatient_UserPatientIdAndStateIn(
                userId.intValue(), List.of(SessionState.PENDING)
        );
        if (hasPending) {
            throw new IllegalStateException("Ya tienes una sesión pendiente. No puedes agendar otra.");
        }

        Optional<ScheduleSession> latestAccepted = scheduledSessionDao
                .findTopByUserPatient_UserPatientIdAndStateOrderByTherapistScheduled_DateDesc(
                        userId.intValue(), SessionState.ACCEPTED
                );

        if (latestAccepted.isPresent()) {
            LocalDateTime scheduledTime = LocalDateTime.of(
                    latestAccepted.get().getTherapistScheduled().getDate(),
                    latestAccepted.get().getTherapistScheduled().getStartTime()
            );
            if (!scheduledTime.isBefore(LocalDateTime.now())) {
                throw new IllegalStateException("Ya tienes una sesión confirmada que aún no ocurrió.");
            }
        }

        ScheduleSession session = new ScheduleSession();
        session.setTherapistScheduled(schedule);
        session.setUserPatient(userPatient);
        session.setState(SessionState.PENDING);
        session.setReason(reason);


        scheduledSessionDao.save(session);
        logger.info("Solicitud de sesión registrada correctamente. Estado inicial: {}", session.getState());
    }

    /**
     * Devuelve próximas citas (ACEPTADAS o PENDIENTES) de un terapeuta, ordenadas y limitadas.
     *
     * @param therapistUserId ID del terapeuta (userId)
     * @param limit número máximo de resultados
     * @return lista de próximas citas como DTOs
     */
    @Transactional(readOnly = true)
    public List<UpcomingAppointmentDto> getUpcomingAppointmentsForTherapist(Long therapistUserId, int limit) {
        List<ScheduleSession> sessions = scheduledSessionDao
                .findByTherapistScheduled_UserTherapistIdAndStateIn(
                        therapistUserId.intValue(),
                        List.of(SessionState.PENDING, SessionState.ACCEPTED, SessionState.COMPLETED)
                );

        LocalDateTime now = LocalDateTime.now();

        return sessions.stream()
                .map(this::mapSessionToDto)
                .filter(dto -> {
                    LocalDateTime endTime = dto.getDateTime().plusHours(1);
                    boolean isUpcoming = now.isBefore(endTime); // sesiones activas o futuras
                    boolean isGraceCompleted = "COMPLETED".equals(dto.getState()) &&
                            now.isBefore(endTime.plusMinutes(10)); // completadas hace poco

                    return isUpcoming || isGraceCompleted;
                })
                .sorted(Comparator.comparing(UpcomingAppointmentDto::getDateTime))
                .limit(limit)
                .toList();
    }


    /**
     * Devuelve las sesiones PENDING futuras de un terapeuta.
     *
     * @param therapistUserId ID del terapeuta
     * @return lista de citas pendientes futuras
     */
    @Transactional(readOnly = true)
    public List<UpcomingAppointmentDto> getPendingAppointmentsForTherapist(Long therapistUserId) {
        List<ScheduleSession> sessions = scheduledSessionDao
                .findByTherapistScheduled_UserTherapistIdAndStateIn(
                        therapistUserId.intValue(), List.of(SessionState.PENDING)
                );

        LocalDateTime now = LocalDateTime.now();

        return sessions.stream()
                .map(this::mapSessionToDto)
                .filter(dto -> !dto.getDateTime().isBefore(now))
                .sorted(Comparator.comparing(UpcomingAppointmentDto::getDateTime))
                .toList();
    }

    /**
     * Marca una sesión de tipo suelta (fuera de tratamiento) como COMPLETED.
     * Aplica validaciones para evitar marcar sesiones futuras o ya marcadas.
     *
     * @param sessionId ID de la sesión a marcar como completada.
     */
    @Transactional
    public void markScheduleSessionAsCompleted(Long sessionId) {
        logger.info("Solicitando marcar como COMPLETED la sesión ID={}", sessionId);

        ScheduleSession session = scheduledSessionDao.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("La sesión no existe."));

        // Validar que no esté ya en estado COMPLETED
        if (session.getState() == SessionState.COMPLETED) {
            logger.warn("La sesión ID={} ya fue marcada como COMPLETED", sessionId);
            throw new IllegalStateException("Esta sesión ya fue completada.");
        }

        // Validar que no sea parte de un tratamiento (debe usarse otro método en ese caso)
        if (session.getTreatmentSessions() != null && !session.getTreatmentSessions().isEmpty()) {
            logger.warn("La sesión ID={} pertenece a un tratamiento, se debe marcar desde TreatmentSessionBl", sessionId);
            throw new IllegalStateException("Esta sesión forma parte de un tratamiento y debe completarse desde el módulo correspondiente.");
        }


        // Marcar como completada
        session.setState(SessionState.COMPLETED);
        scheduledSessionDao.save(session);
        logger.info("Sesión ID={} marcada como COMPLETED exitosamente.", sessionId);
    }
    /**
     * Devuelve todas las sesiones individuales del terapeuta, sin filtros de fecha o estado.
     *
     * @param therapistUserId ID del terapeuta (userId)
     * @return lista de todas las sesiones como DTOs
     */
    @Transactional(readOnly = true)
    public List<UpcomingAppointmentDto> getAllAppointmentsForTherapist(Long therapistUserId) {
        List<ScheduleSession> sessions = scheduledSessionDao
                .findByTherapistScheduled_UserTherapistId(therapistUserId.intValue());

        return sessions.stream()
                .map(this::mapSessionToDto)
                .sorted(Comparator.comparing(UpcomingAppointmentDto::getDateTime))
                .toList();
    }

    /**
     * Convierte una entidad ScheduleSession a DTO para vista de agenda.
     */
    private UpcomingAppointmentDto mapSessionToDto(ScheduleSession s) {
        var sched = s.getTherapistScheduled();
        LocalDateTime dateTime = sched.getDate().atTime(sched.getStartTime());

        String studentName = s.getUserPatient().getUser().getFirstName()
                + " " + s.getUserPatient().getUser().getLastName();

        Long patientId = s.getUserPatient().getUserPatientId().longValue();
        Long therapistId = (long) sched.getUserTherapistId();
        boolean isPartOfTreatment = s.getTreatmentSessions() != null && !s.getTreatmentSessions().isEmpty();

        return new UpcomingAppointmentDto(
                patientId,
                therapistId,
                s.getScheduleSessionId(),
                studentName,
                dateTime,
                s.getState().name(),
                isPartOfTreatment,
                s.getReason()
        );
    }

    /**
     * Permite al terapeuta aceptar o rechazar una sesión solicitada por un paciente.
     * Si se acepta, se publica un evento de confirmación y se programa un recordatorio.
     * Si se rechaza, se publica un evento de rechazo.
     *
     * @param sessionId ID de la sesión
     * @param newState nuevo estado (solo se permiten ACCEPTED o REJECTED)
     */
    public void updateSessionState(Long sessionId, SessionState newState) {
        logger.info("Solicitando cambio de estado para sesión ID={} a '{}'", sessionId, newState);

        if (newState != SessionState.ACCEPTED && newState != SessionState.REJECTED) {
            throw new IllegalArgumentException("El estado solo puede ser ACCEPTED o REJECTED.");
        }

        ScheduleSession session = scheduledSessionDao.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la sesión solicitada."));

        if (session.getState() != SessionState.PENDING) {
            throw new IllegalStateException("Solo es posible modificar sesiones en estado PENDING.");
        }

        session.setState(newState);
        scheduledSessionDao.save(session);
        logger.info("Estado de la sesión ID={} actualizado a '{}'", sessionId, newState);

        if (newState == SessionState.ACCEPTED) {
            handleAccepted(session);
        } else {
            handleRejected(session);
        }
    }

    /**
     * Publica eventos correspondientes cuando se acepta una sesión,
     * incluyendo recordatorio diferido si la sesión es en más de 24 horas.
     *
     * @param session sesión aceptada
     */
    private void handleAccepted(ScheduleSession session) {
        CommonEventData data = buildCommonEventData(session);

        // Evento de cita confirmada
        AppointmentBookedEvent booked = new AppointmentBookedEvent(
                data.appointmentId(),
                data.patientId(),
                data.therapistId(),
                data.appointmentTime(),
                data.patientWhatsapp(),
                data.therapistName()
        );
        eventPublisher.publishBooked(booked);
        logger.info("AppointmentBookedEvent enviado: {}", booked);

        // Recordatorio un día antes
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dayBefore = data.appointmentTime().minusDays(1);
        long delayMs = Duration.between(now, dayBefore).toMillis();

        if (delayMs > 0) {
            AppointmentReminderEvent reminder = new AppointmentReminderEvent(
                    data.appointmentId(),
                    data.patientId(),
                    data.appointmentTime(),
                    dayBefore,
                    data.patientWhatsapp(),
                    data.therapistName()
            );
            eventPublisher.publishReminderDelayed(reminder, delayMs);
            logger.info("Reminder un día antes programado con TTL={}ms para {}", delayMs, dayBefore);
        } else {
            logger.info("Cita en menos de 24h ({}), no se programa recordatorio", Duration.between(now, data.appointmentTime()));
        }
    }

    /**
     * Publica evento correspondiente cuando se rechaza una sesión solicitada.
     *
     * @param session sesión rechazada
     */
    private void handleRejected(ScheduleSession session) {
        CommonEventData data = buildCommonEventData(session);

        AppointmentRejectedEvent rejected = new AppointmentRejectedEvent(
                data.appointmentId(),
                data.patientId(),
                data.therapistId(),
                data.appointmentTime(),
                data.patientWhatsapp(),
                data.therapistName()
        );
        eventPublisher.publishRejected(rejected);
        logger.info("AppointmentRejectedEvent enviado: {}", rejected);
        Long patientId = session.getUserPatient().getUserPatientId();

        // Contar sesiones rechazadas activas
        int rejectionCount = scheduledSessionDao
                .countByUserPatient_UserPatientIdAndState(patientId, SessionState.REJECTED);

        logger.info("Paciente {} ha sido rechazado {} vez/veces", patientId, rejectionCount);

        // Si es el segundo rechazo, bloquear por 24 horas
        if (rejectionCount >= 2) {
            UserPatient patient = session.getUserPatient();
            LocalDateTime blockUntil = LocalDateTime.now().plusHours(24);
            patient.setBlockedUntil(blockUntil);
            userPatientDao.save(patient);

            logger.warn("Paciente {} bloqueado hasta {}", patientId, blockUntil);
        }
    }

    /**
     * Extrae y empaqueta todos los datos necesarios para eventos de cita (aceptada o rechazada).
     *
     * @param session sesión a procesar
     * @return estructura interna con datos de evento
     */
    private CommonEventData buildCommonEventData(ScheduleSession session) {
        Long appointmentId = session.getScheduleSessionId();
        Long patientId = session.getUserPatient().getUserPatientId();
        Long therapistId = (long) session.getTherapistScheduled().getUserTherapistId();

        LocalDateTime appointmentTime = session.getTherapistScheduled()
                .getDate()
                .atTime(session.getTherapistScheduled().getStartTime());

        String rawPhone = session.getUserPatient().getUser().getPhoneNumber();
        String patientWhatsapp = "whatsapp:+591" + rawPhone;

        String therapistName = userTherapistDao.findById(therapistId)
                .map(ut -> ut.getUser().getFirstName() + " " + ut.getUser().getLastName())
                .orElseThrow(() -> new IllegalStateException("Terapeuta no encontrado: " + therapistId));

        return new CommonEventData(appointmentId, patientId, therapistId, appointmentTime, patientWhatsapp, therapistName);
    }

    /**
     * Estructura interna para encapsular los datos necesarios para los eventos de cita.
     */
    private record CommonEventData(
            Long appointmentId,
            Long patientId,
            Long therapistId,
            LocalDateTime appointmentTime,
            String patientWhatsapp,
            String therapistName
    ) {}
}

