package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.*;
import bo.com.ucb.psymanager.dto.ScheduleAvailabilityDto;
import bo.com.ucb.psymanager.dto.TreatmentSessionDto;
import bo.com.ucb.psymanager.entities.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Lógica de negocio para sesiones dentro de un plan de tratamiento.
 * Maneja asignación, cancelación, reordenamiento y marcado de sesiones.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TreatmentSessionBl {

    private final TreatmentDao treatmentDao;
    private final TreatmentSessionDao treatmentSessionDao;
    private final ScheduledSessionDao scheduleSessionDao;
    private final TherapistScheduledDao therapistScheduledDao;
    private final UserTherapistDao userTherapistDao;

    /**
     * Asigna sesiones disponibles (ScheduleSession) a un plan de tratamiento.
     *
     * @param planId ID del tratamiento
     * @param tsIds lista de IDs de horarios seleccionados
     * @return lista de sesiones creadas como DTOs
     */
    @Transactional
    public List<TreatmentSessionDto> addSessionsToPlan(Long planId, List<Long> tsIds) {
        log.info("Asignando {} sesiones al plan ID={}", tsIds.size(), planId);

        Treatment plan = treatmentDao.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado: " + planId));

        Long therapistId = plan.getUserTherapist().getUserTherapistId();
        List<TreatmentSessionDto> created = new ArrayList<>();
        int order = treatmentSessionDao.countByTreatment_TreatmentId(planId) + 1;

        for (Long tsId : tsIds) {
            TherapistScheduled ts = therapistScheduledDao.findById(tsId)
                    .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado: " + tsId));

            if (!Objects.equals(ts.getUserTherapistId(), therapistId.intValue())) {
                throw new IllegalArgumentException("El horario ID=" + tsId + " no pertenece al terapeuta actual");
            }

            boolean ocupado = scheduleSessionDao.existsByTherapistScheduled_TherapistScheduledIdAndStateIn(
                    ts.getTherapistScheduledId(),
                    List.of(SessionState.ACCEPTED, SessionState.PENDING)
            );
            if (ocupado) {
                throw new IllegalStateException("El horario ID=" + tsId + " ya está ocupado.");
            }

            ScheduleSession newSlot = new ScheduleSession();
            newSlot.setTherapistScheduled(ts);
            newSlot.setUserPatient(plan.getUserPatient());
            newSlot.setState(SessionState.ACCEPTED);
            scheduleSessionDao.save(newSlot);

            TreatmentSession session = new TreatmentSession();
            session.setTreatment(plan);
            session.setScheduleSession(newSlot);
            session.setSessionOrder(order++);
            session.setCompleted(false);
            session.setNotes(null);
            treatmentSessionDao.save(session);

            log.info("Sesión creada: planId={}, slotId={}, therapistScheduledId={}", planId, newSlot.getScheduleSessionId(), tsId);
            created.add(mapToDto(session));
        }

        updatePlanMetadata(planId);
        return created;
    }

    private TreatmentSessionDto mapToDto(TreatmentSession session) {
        TreatmentSessionDto dto = new TreatmentSessionDto();
        dto.setId(session.getTreatmentSessionId());
        dto.setTreatmentPlanId(session.getTreatment().getTreatmentId());

        TherapistScheduled ts = session.getScheduleSession().getTherapistScheduled();
        dto.setScheduledDateTime(ts.getDate().atTime(ts.getStartTime()));
        dto.setState(session.getScheduleSession().getState().name());

        return dto;
    }

    /**
     * Cancela una sesión individual de tratamiento y actualiza metadatos del plan.
     *
     * @param sessionId ID de la sesión a eliminar
     */
    @Transactional
    public void cancelTreatmentSession(Long sessionId) {
        log.info("Cancelando sesión de tratamiento con ID={}", sessionId);

        TreatmentSession session = treatmentSessionDao.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Sesión no encontrada: " + sessionId));

        Long planId = session.getTreatment().getTreatmentId();
        treatmentSessionDao.delete(session);
        log.info("Sesión ID={} eliminada del plan ID={}", sessionId, planId);

        updatePlanMetadata(planId);
    }

    /**
     * Recalcula y actualiza la fecha de fin del tratamiento con base en sus sesiones actuales.
     *
     * @param planId ID del plan de tratamiento
     */
    @Transactional
    public void updatePlanMetadata(Long planId) {
        Treatment plan = treatmentDao.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado: " + planId));

        List<TreatmentSession> sessions = treatmentSessionDao.findByTreatment_TreatmentId(planId);

        if (sessions.isEmpty()) {
            log.warn("El plan ID={} ya no tiene sesiones asignadas. Se asigna endDate = startDate ({})", planId, plan.getStartDate());
            plan.setEndDate(plan.getStartDate());
        } else {
            LocalDate latestDate = sessions.stream()
                    .map(s -> s.getScheduleSession().getTherapistScheduled().getDate())
                    .max(Comparator.naturalOrder())
                    .orElse(plan.getStartDate());

            plan.setEndDate(latestDate);
            log.info("Plan ID={} actualizado. Total sesiones: {}. Nueva fecha de fin: {}", planId, sessions.size(), latestDate);
        }

        treatmentDao.save(plan);
    }

    /**
     * Permite al terapeuta cancelar una sesión futura no completada de un tratamiento.
     *
     * @param treatmentId ID del tratamiento
     * @param sessionId   ID de la sesión a cancelar
     */
    @Transactional
    public void cancelFutureSession(Long treatmentId, Long sessionId) {
        TreatmentSession session = treatmentSessionDao.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Sesión no encontrada: " + sessionId));

        if (!session.getTreatment().getTreatmentId().equals(treatmentId)) {
            throw new IllegalArgumentException("La sesión no pertenece al tratamiento especificado.");
        }

        if (session.getCompleted()) {
            throw new IllegalStateException("No se puede cancelar una sesión ya completada.");
        }

        LocalDate sessionDate = session.getScheduleSession().getTherapistScheduled().getDate();
        if (!sessionDate.isAfter(LocalDate.now())) {
            throw new IllegalStateException("Solo se pueden cancelar sesiones futuras.");
        }

        ScheduleSession schedule = session.getScheduleSession();
        schedule.setState(SessionState.CANCELED);
        scheduleSessionDao.save(schedule);

        treatmentSessionDao.delete(session);

        // Reordenar sesiones restantes
        List<TreatmentSession> restantes = treatmentSessionDao
                .findByTreatment_TreatmentIdOrderBySessionOrderAsc(treatmentId);

        for (int i = 0; i < restantes.size(); i++) {
            restantes.get(i).setSessionOrder(i + 1);
        }

        treatmentSessionDao.saveAll(restantes);
        log.info("Sesión ID={} eliminada. Reordenadas {} sesiones para tratamiento ID={}", sessionId, restantes.size(), treatmentId);
    }

    /**
     * Marca una sesión como completada si ya ocurrió.
     *
     * @param sessionId ID de la sesión
     */
    @Transactional
    public void markSessionAsCompleted(Long sessionId) {
        TreatmentSession session = treatmentSessionDao.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Sesión no encontrada: " + sessionId));

        LocalDateTime endDateTime = session.getScheduleSession()
                .getTherapistScheduled()
                .getDate()
                .atTime(session.getScheduleSession().getTherapistScheduled().getEndTime());

        if (endDateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("No puedes marcar como completada una sesión que aún no terminó.");
        }

        if (session.getCompleted()) {
            throw new IllegalStateException("La sesión ya fue marcada como completada.");
        }

        session.setCompleted(true);
        treatmentSessionDao.save(session);
        log.info("Sesión ID={} marcada como completada.", sessionId);
    }

    /**
     * Obtiene las sesiones programadas desde el tratamiento activo de un paciente.
     *
     * @param patientId ID del paciente
     * @return lista de sesiones asignadas como disponibilidad
     */
    @Transactional
    public List<ScheduleAvailabilityDto> getScheduledSessionsFromActiveTreatment(Long patientId) {
        log.info("Buscando sesiones del tratamiento activo del paciente ID={}", patientId);

        Treatment treatment = treatmentDao.findActiveTreatmentByPatientId(patientId)
                .orElseThrow(() -> new IllegalStateException("El paciente no tiene tratamiento activo."));

        List<TreatmentSession> sessions = treatmentSessionDao.findByTreatment_TreatmentId(treatment.getTreatmentId());

        List<ScheduleAvailabilityDto> dtos = sessions.stream()
                .map(TreatmentSession::getScheduleSession)
                .map(this::mapToAvailabilityDto)
                .toList();

        log.info("Paciente ID={} tiene {} sesiones asignadas.", patientId, dtos.size());
        return dtos;
    }

    /**
     * Convierte una ScheduleSession a un DTO de disponibilidad de horario.
     */
    private ScheduleAvailabilityDto mapToAvailabilityDto(ScheduleSession session) {
        TherapistScheduled ts = session.getTherapistScheduled();

        Long therapistId = (long) ts.getUserTherapistId();
        String therapistName = userTherapistDao.findById(therapistId)
                .map(ut -> ut.getUser().getFirstName() + " " + ut.getUser().getLastName())
                .orElse("Terapeuta");

        return new ScheduleAvailabilityDto(
                session.getScheduleSessionId(),
                ts.getUserTherapistId(),
                ts.getDate(),
                ts.getStartTime().toString(),
                ts.getEndTime().toString(),
                "treatment-assigned",
                therapistName,
                session.getUserPatient().getUserPatientId(),
                session.getState()
        );
    }
}

