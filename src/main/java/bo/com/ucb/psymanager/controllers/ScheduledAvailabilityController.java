package bo.com.ucb.psymanager.controllers;

import bo.com.ucb.psymanager.bl.ScheduledAvailabilityBl;
import bo.com.ucb.psymanager.dto.ScheduleAvailabilityDto;
import bo.com.ucb.psymanager.dto.ScheduleAvailabilityWithContactDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8081")
public class ScheduledAvailabilityController {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledAvailabilityController.class);

    private final ScheduledAvailabilityBl scheduledAvailabilityBl;

    /**
     * Devuelve sólo los horarios cuya fecha esté entre startDate y endDate.
     * Ejemplo:
     *   GET /api/schedules/available?startDate=2025-04-28&endDate=2025-05-02
     */
    @GetMapping("/available")
    public ResponseEntity<List<ScheduleAvailabilityDto>> getSchedulesInRange(
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        logger.info("Buscando horarios entre {} y {}", startDate, endDate);
        List<ScheduleAvailabilityDto> schedules =
                scheduledAvailabilityBl.getSchedulesInRange(startDate, endDate);
        return ResponseEntity.ok(schedules);
    }

    /**
     * Obtiene todos los horarios registrados junto con su estado de disponibilidad.
     *
     * @return Lista de horarios con estado "available" o "taken".
     */
    @GetMapping("/filterByTherapistDate")
    public ResponseEntity<List<ScheduleAvailabilityDto>> getSchedulesWithOptionalFilters(
            @RequestParam(name = "therapistId", required = false) Integer therapistId,
            @RequestParam(name = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        logger.info("Filtro por terapeuta entre fechas: {} - {}, terapeutaId={}", startDate, endDate, therapistId);
        List<ScheduleAvailabilityDto> result =
                scheduledAvailabilityBl.getSchedulesWithOptionalFilters(therapistId, startDate, endDate);
        return ResponseEntity.ok(result);
    }

    /**
     * Obtiene los horarios disponibles del mismo terapeuta y fecha a partir del ID de un horario.
     *
     * @param scheduleId ID del horario base seleccionado.
     * @return Lista de horarios disponibles asociados al terapeuta y fecha.
     */
    @GetMapping("/available/by-schedule/{scheduleId}")
    public ResponseEntity<List<ScheduleAvailabilityWithContactDto>> getSchedulesByScheduleId(
            @PathVariable Long scheduleId) {

        logger.info("Solicitud de horarios disponibles por scheduleId={}", scheduleId);
        List<ScheduleAvailabilityWithContactDto> result = scheduledAvailabilityBl.getAvailableSchedulesByScheduleId(scheduleId);
        return ResponseEntity.ok(result);
    }

}
