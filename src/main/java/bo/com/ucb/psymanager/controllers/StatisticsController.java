package bo.com.ucb.psymanager.controllers;

import bo.com.ucb.psymanager.bl.CompletedExerciseBl;
import bo.com.ucb.psymanager.dto.DailySeriesResponseDto;
import bo.com.ucb.psymanager.dto.HourlySeriesResponseDto;
import bo.com.ucb.psymanager.dto.StatisticsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Controlador REST para exponer estadísticas de uso de ejercicios
 * completados por los pacientes.
 */
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('THERAPIST')")
public class StatisticsController {

    private final CompletedExerciseBl completedExerciseBl;

    /**
     * Obtiene las métricas semanales y por categoría para un paciente
     * en el rango de fechas indicado.
     *
     * @param patientId ID del paciente a consultar
     * @param from      Fecha de inicio del período (ISO-8601, p. ej. 2025-06-01T00:00:00)
     * @param to        Fecha de fin   del período (ISO-8601, p. ej. 2025-06-07T23:59:59)
     * @return 200 OK con el StatisticsResponseDto en el cuerpo
     */
    @GetMapping("/{patientId}/weekly")
    public ResponseEntity<StatisticsResponseDto> getWeeklyStatistics(
            @PathVariable Long patientId,
            @RequestParam("from")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,
            @RequestParam("to")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to
    ) {
        log.info("Solicitando estadísticas semanales para pacienteId={} desde {} hasta {}", patientId, from, to);
        StatisticsResponseDto stats = completedExerciseBl.getWeeklyStatistics(patientId, from, to);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/{patientId}/daily")
    public ResponseEntity<DailySeriesResponseDto> getDailyStats(
            @PathVariable Long patientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        DailySeriesResponseDto dto = completedExerciseBl.getDailyStatistics(patientId, from, to);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{patientId}/hourly")
    public ResponseEntity<HourlySeriesResponseDto> getHourlyStats(
            @PathVariable Long patientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        var dto = completedExerciseBl.getHourlyStatistics(patientId, from, to);
        return ResponseEntity.ok(dto);
    }
}