package bo.com.ucb.psymanager.controllers;

import bo.com.ucb.psymanager.bl.TreatmentSessionBl;
import bo.com.ucb.psymanager.dto.CreateTreatmentSessionsRequestDto;
import bo.com.ucb.psymanager.dto.TreatmentSessionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para manejar sesiones asignadas a tratamientos.
 * Incluye asignación, cancelación y marcado como completado.
 */
@RestController
@RequestMapping("/api/treatments")
@RequiredArgsConstructor
@Slf4j
public class TreatmentSessionController {

    private final TreatmentSessionBl treatmentSessionBl;

    /**
     * Añade una o varias sesiones agendadas a un plan de tratamiento existente.
     *
     * @param planId ID del plan
     * @param dto    DTO con lista de IDs de horarios disponibles (slots)
     * @return lista de sesiones creadas como DTOs
     */
    @PostMapping("/{planId}/sessions")
    public ResponseEntity<List<TreatmentSessionDto>> addSessionsToPlan(
            @PathVariable Long planId,
            @RequestBody CreateTreatmentSessionsRequestDto dto
    ) {
        log.info("POST /api/treatments/{}/sessions → slots asignados: {}", planId, dto.getSlotIds().size());
        List<TreatmentSessionDto> created = treatmentSessionBl.addSessionsToPlan(planId, dto.getSlotIds());
        return ResponseEntity.ok(created);
    }

    /**
     * Cancela una sesión futura asociada a un tratamiento activo.
     *
     * @param treatmentId ID del tratamiento
     * @param sessionId   ID de la sesión a eliminar
     * @return respuesta sin contenido
     */
    @DeleteMapping("/{treatmentId}/sessions/{sessionId}")
    public ResponseEntity<Void> cancelTreatmentSession(
            @PathVariable Long treatmentId,
            @PathVariable Long sessionId
    ) {
        log.info("DELETE /api/treatments/{}/sessions/{} → cancelar sesión", treatmentId, sessionId);
        treatmentSessionBl.cancelFutureSession(treatmentId, sessionId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Marca una sesión como completada si ya ha finalizado.
     *
     * @param sessionId ID de la sesión
     * @return respuesta sin contenido
     */
    @PutMapping("/sessions/{sessionId}/complete")
    public ResponseEntity<Void> completeSession(@PathVariable Long sessionId) {
        log.info("PUT /api/treatments/sessions/{}/complete → marcar como completada", sessionId);
        treatmentSessionBl.markSessionAsCompleted(sessionId);
        return ResponseEntity.noContent().build();
    }
}
