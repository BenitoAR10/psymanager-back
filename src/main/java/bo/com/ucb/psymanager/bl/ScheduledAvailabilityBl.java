package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.ScheduledSessionDao;
import bo.com.ucb.psymanager.dao.TherapistScheduledDao;
import bo.com.ucb.psymanager.dao.UserDao;
import bo.com.ucb.psymanager.dto.ScheduleAvailabilityDto;
import bo.com.ucb.psymanager.dto.ScheduleAvailabilityWithContactDto;
import bo.com.ucb.psymanager.entities.ScheduleSession;
import bo.com.ucb.psymanager.entities.SessionState;
import bo.com.ucb.psymanager.entities.TherapistScheduled;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ScheduledAvailabilityBl {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledAvailabilityBl.class);

    private final TherapistScheduledDao therapistScheduledDao;
    private final ScheduledSessionDao scheduledSessionDao;
    private final UserDao userDao;


    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Obtiene todos los horarios de los terapeutas junto con su estado de disponibilidad.
     * Un horario se considera "taken" si ya fue reservado por un paciente (state = 'taken').
     *
     * @return Lista de horarios con información de disponibilidad.
     */
    public List<ScheduleAvailabilityDto> getAllSchedulesWithAvailability() {
        logger.info("Iniciando la recolección de todos los horarios registrados con su disponibilidad...");

        return therapistScheduledDao.findAll().stream().map(this::buildDto).toList();
    }


    /**
     * Obtiene todos los horarios de los terapeutas junto con su estado de disponibilidad.
     * Un horario se considera "taken" si ya fue reservado por un paciente (state = 'taken').
     *
     * @param therapistId ID del terapeuta (opcional)
     * @return Lista de horarios con información de disponibilidad.
     */

    public List<ScheduleAvailabilityDto> getSchedulesWithOptionalFilters(
            Integer therapistId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        logger.info("Buscando horarios entre {} y {}, para therapistId={}", startDate, endDate, therapistId);

        return therapistScheduledDao.findAll().stream()
                .filter(s -> therapistId == null || s.getUserTherapistId() == therapistId)
                .filter(s -> {
                    if (startDate != null && endDate != null) {
                        return !s.getDate().isBefore(startDate) && !s.getDate().isAfter(endDate);
                    } else if (startDate != null) {
                        return !s.getDate().isBefore(startDate);
                    } else if (endDate != null) {
                        return !s.getDate().isAfter(endDate);
                    }
                    return true;
                })
                .map(this::buildDto)
                .toList();
    }


    /**
     * Obtiene los horarios disponibles de un terapeuta específico según el ID del horario base (scheduleId),
     * incluyendo información de contacto del terapeuta.
     *
     * @param scheduleId ID del horario seleccionado.
     * @return Lista de horarios disponibles del mismo terapeuta para esa fecha, con contacto.
     */
    public List<ScheduleAvailabilityWithContactDto> getAvailableSchedulesByScheduleId(Long scheduleId) {
        logger.info("Obteniendo horarios disponibles relacionados al scheduleId={} (con contacto)", scheduleId);

        TherapistScheduled baseSchedule = therapistScheduledDao.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        int therapistId = baseSchedule.getUserTherapistId();
        LocalDate date = baseSchedule.getDate();

        return therapistScheduledDao.findAll().stream()
                .filter(s -> s.getUserTherapistId() == therapistId && s.getDate().isEqual(date))
                .map(this::buildDtoWithContact)
                .toList();
    }

    /**
     * Obtiene los horarios entre startDate y endDate inclusive.
     */
    public List<ScheduleAvailabilityDto> getSchedulesInRange(LocalDate startDate, LocalDate endDate) {
        return therapistScheduledDao.findByDateBetween(startDate, endDate)
                .stream()
                .map(this::buildDto)
                .toList();
    }

    /**
     * Verifica si un horario ya está ocupado (es decir, tiene una sesión en estado PENDING o ACCEPTED).
     * Devuelve el ID del paciente si está ocupado, o null si está libre.
     */
    private Long getUserIdIfScheduleIsOccupied(Long therapistScheduledId) {
        return scheduledSessionDao
                .findByTherapistScheduled_TherapistScheduledId(therapistScheduledId)
                .stream()
                .filter(session -> {
                    SessionState state = session.getState();
                    return state == SessionState.PENDING || state == SessionState.ACCEPTED;
                })
                .peek(session -> logger.debug("Horario ocupado por userId={}", session.getUserPatient().getUser().getUserId()))
                .map(session -> (long) session.getUserPatient().getUser().getUserId())
                .findFirst()
                .orElse(null);
    }

    private ScheduleAvailabilityDto buildDto(TherapistScheduled schedule) {
        Optional<ScheduleSession> maybeSession = scheduledSessionDao
                .findByTherapistScheduled_TherapistScheduledId(schedule.getTherapistScheduledId())
                .stream()
                .filter(session -> session.getState() == SessionState.PENDING || session.getState() == SessionState.ACCEPTED)
                .findFirst();

        Long reservedByUserId = maybeSession.map(s -> (long) s.getUserPatient().getUser().getUserId()).orElse(null);
        SessionState sessionState = maybeSession.map(ScheduleSession::getState).orElse(null);

        String therapistName = userDao.findById((long) schedule.getUserTherapistId())
                .map(user -> user.getFirstName() + " " + user.getLastName())
                .orElse("Terapeuta no encontrado");

        return new ScheduleAvailabilityDto(
                schedule.getTherapistScheduledId(),
                schedule.getUserTherapistId(),
                schedule.getDate(),
                schedule.getStartTime().format(timeFormatter),
                schedule.getEndTime().format(timeFormatter),
                reservedByUserId != null ? "taken" : "available",
                therapistName,
                reservedByUserId,
                sessionState
        );
    }


    private ScheduleAvailabilityWithContactDto buildDtoWithContact(TherapistScheduled schedule) {
        Optional<ScheduleSession> maybeSession = scheduledSessionDao
                .findByTherapistScheduled_TherapistScheduledId(schedule.getTherapistScheduledId())
                .stream()
                .filter(session -> session.getState() == SessionState.PENDING || session.getState() == SessionState.ACCEPTED)
                .findFirst();

        Long reservedByUserId = maybeSession
                .map(s -> (long) s.getUserPatient().getUser().getUserId())
                .orElse(null);

        SessionState sessionState = maybeSession
                .map(ScheduleSession::getState)
                .orElse(null);

        return userDao.findById((long) schedule.getUserTherapistId())
                .map(user -> new ScheduleAvailabilityWithContactDto(
                        schedule.getTherapistScheduledId(),
                        schedule.getUserTherapistId(),
                        schedule.getDate(),
                        schedule.getStartTime().format(timeFormatter),
                        schedule.getEndTime().format(timeFormatter),
                        reservedByUserId != null ? "taken" : "available",
                        user.getFirstName() + " " + user.getLastName(),
                        reservedByUserId,
                        sessionState,
                        user.getPhoneNumber(),
                        user.getEmail()
                ))
                .orElseThrow(() -> new RuntimeException("Usuario terapeuta no encontrado"));
    }




}