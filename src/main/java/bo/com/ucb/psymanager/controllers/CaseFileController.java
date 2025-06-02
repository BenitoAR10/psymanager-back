package bo.com.ucb.psymanager.controllers;


import bo.com.ucb.psymanager.bl.CaseFileBl;
import bo.com.ucb.psymanager.dto.CaseFileDto;
import bo.com.ucb.psymanager.dto.CreateOrUpdateCaseFileRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * Controlador REST para la gestion de expendiente final (case file) de un tratamiento cerrado
 */

@RestController
@RequestMapping("/api/case-files")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('THERAPIST')")
public class CaseFileController {

    private final CaseFileBl caseFileBl;

    /**
     * Registra un expediente final para un tratamiento cerrado.
     *
     * @param dto         Datos del expediente (fecha, conclusiones)
     * @return Detalles del expediente creado
     */
    @PostMapping
    public ResponseEntity<CaseFileDto> saveOrUpdateCaseFile(@RequestBody CreateOrUpdateCaseFileRequestDto dto) {
        log.info("POST /api/case-files - Crear o actualizar ficha clínica");
        return ResponseEntity.ok(caseFileBl.createOrUpdateCaseFile(dto));
    }

    /**
     * Obtiene el expediente registrado para un tratamiento cerrado.
     *
     * @param treatmentId ID del tratamiento
     * @return Detalles del expediente
     */
    @GetMapping("/treatment/{treatmentId}")
    public ResponseEntity<CaseFileDto> getCaseFileByTreatment(@PathVariable Long treatmentId) {
        log.info("GET /api/case-files/treatment/{}", treatmentId);
        return caseFileBl.getCaseFileByTreatment(treatmentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
