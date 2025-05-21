package bo.com.ucb.psymanager.controllers;

import bo.com.ucb.psymanager.bl.TherapistProfileBl;
import bo.com.ucb.psymanager.dto.TherapistProfileUpdateDto;
import bo.com.ucb.psymanager.dto.TherapistProfileViewDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/therapists/me")
public class TherapistProfileController {

    private static final Logger logger = Logger.getLogger(TherapistProfileController.class);

    private final TherapistProfileBl therapistProfileBl;

    @Autowired
    public TherapistProfileController(TherapistProfileBl therapistProfileBl) {
        this.therapistProfileBl = therapistProfileBl;
    }

    /**
     * Permite al terapeuta completar su perfil profesional incluyendo datos personales y especialidades.
     *
     * @param principal información del usuario autenticado
     * @param dto       datos del perfil a completar
     * @return respuesta vacía si fue exitoso
     */
    @PutMapping("/profile")
    @PreAuthorize("hasRole('THERAPIST')")
    public ResponseEntity<Void> updateProfile(Principal principal,
                                              @RequestBody TherapistProfileUpdateDto dto) {
        String email = principal.getName();
        logger.info("Solicitud para actualizar perfil del terapeuta autenticado: " + email);

        therapistProfileBl.updateTherapistProfile(email, dto);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    /**
     * Devuelve los datos del perfil del terapeuta autenticado.
     *
     * @param principal información del usuario autenticado (extraída desde el JWT)
     * @return perfil completo del terapeuta (datos personales y especialidades)
     */
    @GetMapping("/profile")
    @PreAuthorize("hasRole('THERAPIST')")
    public ResponseEntity<TherapistProfileViewDto> getProfile(Principal principal) {
        String email = principal.getName(); // obtenido desde el JWT
        logger.info("Solicitud para obtener perfil del terapeuta autenticado: " + email);

        TherapistProfileViewDto profileDto = therapistProfileBl.getTherapistProfile(email);
        return ResponseEntity.ok(profileDto);
    }


}