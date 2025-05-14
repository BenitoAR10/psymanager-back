package bo.com.ucb.psymanager.controllers;

import bo.com.ucb.psymanager.bl.TherapistScheduledBl;
import bo.com.ucb.psymanager.dto.CreateScheduleRequestDto;
import bo.com.ucb.psymanager.dto.ScheduleResponseDto;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST para gestionar los bloques de disponibilidad de los terapeutas.
 */
@RestController
@RequestMapping("/api/schedules")
@CrossOrigin(origins = "http://localhost:5173")
public class TherapistScheduleController {

    private static final Logger logger = Logger.getLogger(TherapistScheduleController.class);

    private final TherapistScheduledBl scheduledBl;

    public TherapistScheduleController(TherapistScheduledBl scheduledBl) {
        this.scheduledBl = scheduledBl;
    }

    /**
     * Registra un nuevo bloque de disponibilidad para un terapeuta.
     *
     * @param requestDto datos del horario a registrar
     * @return horario registrado como DTO
     */
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody CreateScheduleRequestDto requestDto) {
        logger.info("POST /api/schedules → terapeutaId=" + requestDto.getUserTherapistId()
                + ", fecha=" + requestDto.getDate()
                + ", horaInicio=" + requestDto.getStartTime()
                + ", horaFin=" + requestDto.getEndTime());

        ScheduleResponseDto response = scheduledBl.createSchedule(requestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza un bloque de horario existente.
     *
     * @param scheduleId ID del horario
     * @param requestDto nuevos datos del horario
     * @return horario actualizado
     */
    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long scheduleId,
            @RequestBody CreateScheduleRequestDto requestDto
    ) {
        logger.info("PUT /api/schedules/" + scheduleId + " → actualización de horario");
        ScheduleResponseDto response = scheduledBl.updateSchedule(scheduleId, requestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Devuelve todos los horarios registrados o, si se especifica, solo los de un terapeuta.
     *
     * @param therapistId (opcional) ID del terapeuta
     * @return lista de horarios
     */
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getSchedules(
            @RequestParam(name = "therapistId", required = false) Integer therapistId
    ) {
        if (therapistId != null) {
            logger.info("GET /api/schedules?therapistId=" + therapistId + " → horarios por terapeuta");
            return ResponseEntity.ok(scheduledBl.getSchedulesByTherapist(therapistId));
        } else {
            logger.info("GET /api/schedules → todos los horarios registrados");
            return ResponseEntity.ok(scheduledBl.getAllSchedules());
        }
    }
}
