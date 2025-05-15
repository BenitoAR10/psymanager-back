package bo.com.ucb.psymanager.controllers;

import bo.com.ucb.psymanager.bl.AuthenticatedUserBl;
import bo.com.ucb.psymanager.dto.CompleteProfileRequestDto;
import bo.com.ucb.psymanager.entities.User;
import bo.com.ucb.psymanager.util.CustomOAuth2User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:19006",
})
public class AuthController {

    private static final Logger logger = Logger.getLogger(AuthController.class);

    private final AuthenticatedUserBl authenticatedUserBl;



    @Autowired
    public AuthController(AuthenticatedUserBl authenticatedUserBl) {
        this.authenticatedUserBl = authenticatedUserBl;

    }

    /**
     * Obtiene la información del usuario autenticado a partir del token JWT.
     * Este endpoint es utilizado para validar la sesión del usuario y verificar
     * si su perfil está completo.
     *
     * @param email Email del usuario autenticado, extraído del token JWT por Spring Security.
     * @return Un JSON con los datos básicos del usuario y el estado de su perfil.
     */
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal String email) {
        logger.info("Solicitud a /auth/me");

        if (email == null) {
            logger.warn("Usuario no autenticado al acceder a /me");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado");
        }

        try {
            User user = authenticatedUserBl.getUserByEmail(email);
            List<String> roles = authenticatedUserBl.getUserRoles(email);

            boolean profileComplete = user.getBirthDate() != null &&
                    user.getCiNumber() != null &&
                    user.getAddress() != null &&
                    user.getPhoneNumber() != null;

            Map<String, Object> response = new HashMap<>();
            response.put("email", user.getEmail());
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());
            response.put("userId", user.getUserId());
            response.put("roles", roles);
            response.put("profileComplete", profileComplete);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error al obtener la información del usuario", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener la información del usuario");
        }
    }


    /**
     * Refresca el access token utilizando el refresh token.
     *
     * @param request Mapa que contiene el campo "refreshToken".
     * @return Nuevo par de tokens si el refresh es válido, o 401 si no lo es.
     */
    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> request) {
        logger.info("Solicitud a /auth/token/refresh");

        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || refreshToken.isBlank()) {
            logger.warn("Refresh token faltante en la solicitud");
            return ResponseEntity.badRequest().body("Se requiere el refresh token");
        }

        return authenticatedUserBl.refreshAccessToken(refreshToken)
                .map(tokens -> {
                    logger.info("Refresh token válido. Nuevos tokens generados.");
                    return ResponseEntity.ok(tokens);
                })
                .orElseGet(() -> {
                    logger.warn("Refresh token inválido o expirado.");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                });
    }

    /**
     * Permite que el usuario autenticado complete la información de su perfil,
     * incluyendo datos personales como CI, dirección, género, etc.
     *
     * @param email Email del usuario autenticado extraído del token JWT.
     * @param profileDto DTO con los campos que debe completar.
     * @return Respuesta 200 si se actualiza correctamente, 500 en caso de error.
     */
    @PutMapping("/complete-profile")
    public ResponseEntity<String> completeProfile(
            @AuthenticationPrincipal String email,
            @RequestBody CompleteProfileRequestDto profileDto
    ) {
        try {
            authenticatedUserBl.completeUserProfile(email, profileDto);
            return ResponseEntity.ok("Perfil completado correctamente");
        } catch (Exception e) {
            logger.error("Error al completar perfil", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al completar el perfil");
        }
    }
}