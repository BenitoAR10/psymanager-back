package bo.com.ucb.psymanager.controllers;

import bo.com.ucb.psymanager.bl.UserWellnessStatsBl;
import bo.com.ucb.psymanager.dto.TotalPointsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para exponer las estadísticas de bienestar de los pacientes.
 */
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Slf4j
public class UserWellnessStatsController {

    private final UserWellnessStatsBl statsBl;

    /**
     * Obtiene el total de puntos acumulados por un paciente dado su ID.
     *
     * @param userPatientId ID del paciente
     * @return DTO con el total de puntos
     */
    @GetMapping("/{id}/wellness-stats")
    public ResponseEntity<TotalPointsResponseDto> getTotalPoints(
            @PathVariable("id") Long userPatientId) {
        log.info("Recibiendo solicitud de total de puntos para paciente ID={}", userPatientId);
        int total = statsBl.getTotalPoints(userPatientId);
        log.info("Devolviendo total de puntos={} para paciente ID={}", total, userPatientId);
        return ResponseEntity.ok(new TotalPointsResponseDto(total));
    }
}