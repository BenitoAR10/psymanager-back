package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.TherapistScheduledDao;
import bo.com.ucb.psymanager.dao.UserDao;
import bo.com.ucb.psymanager.dto.CreateScheduleRequestDto;
import bo.com.ucb.psymanager.dto.ScheduleResponseDto;
import bo.com.ucb.psymanager.entities.TherapistScheduled;
import bo.com.ucb.psymanager.exceptions.InvalidScheduleException;
import bo.com.ucb.psymanager.exceptions.ScheduleConflictException;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Lógica de negocio para la gestión de bloques de disponibilidad de los terapeutas.
 */
@Service
@RequiredArgsConstructor
public class TherapistScheduledBl {

    private static final Logger logger = Logger.getLogger(TherapistScheduledBl.class);

    private final TherapistScheduledDao therapistScheduledDao;
    private final UserDao userDao;

    /**
     * Crea un nuevo bloque de horario para un terapeuta.
     *
     * @param requestDto datos del nuevo bloque
     * @return DTO con la información del bloque creado
     */
    public ScheduleResponseDto createSchedule(CreateScheduleRequestDto requestDto) {
        logger.info("Intentando crear nuevo horario para terapeuta ID=" + requestDto.getUserTherapistId());

        if (!requestDto.getStartTime().isBefore(requestDto.getEndTime())) {
            logger.warn("Hora inválida: start=" + requestDto.getStartTime() + ", end=" + requestDto.getEndTime());
            throw new InvalidScheduleException("La hora de inicio debe ser anterior a la hora de fin");
        }

        boolean hasConflict = !therapistScheduledDao.findConflictingSchedules(
                requestDto.getDate(),
                requestDto.getStartTime(),
                requestDto.getEndTime()
        ).isEmpty();

        if (hasConflict) {
            logger.warn("Conflicto detectado para terapeuta ID=" + requestDto.getUserTherapistId());
            throw new ScheduleConflictException("El horario solicitado ya está ocupado");
        }

        TherapistScheduled scheduled = new TherapistScheduled();
        scheduled.setUserTherapistId(requestDto.getUserTherapistId());
        scheduled.setDate(requestDto.getDate());
        scheduled.setStartTime(requestDto.getStartTime());
        scheduled.setEndTime(requestDto.getEndTime());

        TherapistScheduled saved = therapistScheduledDao.save(scheduled);
        logger.info("Horario creado con éxito. ID=" + saved.getTherapistScheduledId());

        return mapToResponse(saved);
    }

    /**
     * Actualiza un bloque de disponibilidad ya registrado.
     *
     * @param scheduleId ID del bloque a actualizar
     * @param requestDto nuevos datos del bloque
     * @return DTO con el nuevo estado del bloque
     */
    public ScheduleResponseDto updateSchedule(Long scheduleId, CreateScheduleRequestDto requestDto) {
        logger.info("Actualizando horario con ID=" + scheduleId);

        if (!requestDto.getStartTime().isBefore(requestDto.getEndTime())) {
            logger.warn("Hora inválida: start=" + requestDto.getStartTime() + ", end=" + requestDto.getEndTime());
            throw new InvalidScheduleException("La hora de inicio debe ser anterior a la hora de fin");
        }

        TherapistScheduled schedule = therapistScheduledDao.findById(scheduleId)
                .orElseThrow(() -> {
                    logger.error("No se encontró el horario con ID=" + scheduleId);
                    return new RuntimeException("No se encontró el horario");
                });

        boolean hasConflict = therapistScheduledDao.findConflictingSchedules(
                        requestDto.getDate(),
                        requestDto.getStartTime(),
                        requestDto.getEndTime()
                ).stream()
                .anyMatch(conflict -> !conflict.getTherapistScheduledId().equals(scheduleId));

        if (hasConflict) {
            logger.warn("Conflicto detectado al actualizar el horario ID=" + scheduleId);
            throw new ScheduleConflictException("El horario solicitado ya está ocupado");
        }

        schedule.setUserTherapistId(requestDto.getUserTherapistId());
        schedule.setDate(requestDto.getDate());
        schedule.setStartTime(requestDto.getStartTime());
        schedule.setEndTime(requestDto.getEndTime());

        TherapistScheduled saved = therapistScheduledDao.save(schedule);
        logger.info("Horario actualizado correctamente. ID=" + saved.getTherapistScheduledId());

        return mapToResponse(saved);
    }

    /**
     * Obtiene todos los horarios registrados en el sistema.
     *
     * @return lista de horarios como DTOs
     */
    public List<ScheduleResponseDto> getAllSchedules() {
        logger.info("Obteniendo todos los horarios registrados");
        return therapistScheduledDao.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los bloques asignados a un terapeuta específico.
     *
     * @param userTherapistId ID del terapeuta
     * @return lista de horarios como DTOs
     */
    public List<ScheduleResponseDto> getSchedulesByTherapist(int userTherapistId) {
        logger.info("Listando horarios para terapeuta ID=" + userTherapistId);
        return therapistScheduledDao.findByUserTherapistId(userTherapistId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad TherapistScheduled a su DTO correspondiente con nombre de terapeuta.
     *
     * @param schedule entidad de horario
     * @return DTO representando el bloque
     */
    private ScheduleResponseDto mapToResponse(TherapistScheduled schedule) {
        String therapistName = userDao.findById((long) schedule.getUserTherapistId())
                .map(user -> user.getFirstName() + " " + user.getLastName())
                .orElse("Desconocido");

        return new ScheduleResponseDto(
                schedule.getTherapistScheduledId(),
                schedule.getUserTherapistId(),
                schedule.getDate(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                therapistName
        );
    }
}
