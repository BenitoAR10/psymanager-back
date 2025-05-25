package bo.com.ucb.psymanager.controllers;

import bo.com.ucb.psymanager.bl.AuthenticatedUserBl;
import bo.com.ucb.psymanager.bl.UserBl;
import bo.com.ucb.psymanager.dto.UserProfileDto;
import bo.com.ucb.psymanager.dto.UserProfileUpdateDto;
import bo.com.ucb.psymanager.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final AuthenticatedUserBl authenticatedUserBl;
    private final UserBl userBl;

    /**
     * Obtiene el perfil del usuario autenticado.
     *
     * @param email Email del usuario autenticado, extraído del token JWT por Spring Security.
     * @return Un JSON con los datos del perfil del usuario.
     */
    @GetMapping("/me/profile")
    public ResponseEntity<UserProfileDto> getPersonalProfile(@AuthenticationPrincipal String email) {
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            User user = authenticatedUserBl.getUserByEmail(email);

            UserProfileDto profile = new UserProfileDto(
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getBirthDate(),
                    user.getIdentityGender(),
                    user.getAddress(),
                    user.getCiNumber()
            );

            return ResponseEntity.ok(profile);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Permite que el usuario autenticado actualice algunos datos personales.
     *
     * @param email Correo electrónico extraído del token JWT.
     * @param dto Datos a actualizar.
     * @return 200 OK si fue exitoso, o 400/500 si hubo errores.
     */
    @PutMapping("/me")
    public ResponseEntity<?> updateMyProfile(
            @AuthenticationPrincipal String email,
            @RequestBody UserProfileUpdateDto dto
    ) {
        log.info("actualización de perfil para {}", email);

        try {
            userBl.updateProfile(email, dto);
            return ResponseEntity.ok(Map.of("message", "Perfil actualizado correctamente"));
        } catch (IllegalArgumentException e) {
            log.warn("Usuario no encontrado: {}", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al actualizar el perfil del usuario {}: {}", email, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error al actualizar el perfil"));
        }
    }
}
