package bo.com.ucb.psymanager.controllers;

import bo.com.ucb.psymanager.bl.CancelSessionBl;
import bo.com.ucb.psymanager.dto.CancelSessionRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Controlador para la cancelación de sesiones por parte de los pacientes.
 */
@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionCancellationController {

    private static final Logger logger = LoggerFactory.getLogger(SessionCancellationController.class);
    private final CancelSessionBl cancelSessionBl;

    /**
     * Permite a un paciente cancelar una sesión previamente aceptada.
     *
     * @param principal usuario autenticado
     * @param dto       solicitud con ID de sesión y motivo
     * @return 204 si se canceló exitosamente
     */
    @PostMapping("/cancel")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Void> cancelSession(
            Principal principal,
            @RequestBody CancelSessionRequestDto dto
    ) {
        String email = principal.getName(); // Extraído desde JWT por el filtro de autenticación
        logger.info("Solicitud de cancelación recibida para el paciente '{}'", email);

        cancelSessionBl.cancelSessionByPatient(email, dto);

        return ResponseEntity.noContent().build();
    }
}