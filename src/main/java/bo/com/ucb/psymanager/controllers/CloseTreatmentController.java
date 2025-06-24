package bo.com.ucb.psymanager.controllers;


import bo.com.ucb.psymanager.bl.CloseTreatmentBl;
import bo.com.ucb.psymanager.dto.CloseTreatmentDto;
import bo.com.ucb.psymanager.dto.CreateCloseTreatmentRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/treatments")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAnyRole('THERAPIST', 'INTERN-THERAPIST')")
public class CloseTreatmentController {

    private final CloseTreatmentBl closeTreatmentBl;

    /**
     * Cierra un tratamiento activo si cumple con todas las validaciones.
     *
     * @param treatmentId ID del tratamiento a cerrar
     * @param dto         Datos del cierre (motivo y fecha)
     * @return DTO con la información del cierre registrado
     */
    @PostMapping("/{treatmentId}/close")
    public ResponseEntity<CloseTreatmentDto> closeTreatment(
            @PathVariable Long treatmentId,
            @RequestBody CreateCloseTreatmentRequestDto dto
    ) {
        log.info("POST /api/treatments/{}/close", treatmentId);
        CloseTreatmentDto result = closeTreatmentBl.closeTreatment(treatmentId, dto);
        return ResponseEntity.ok(result);
    }
}
