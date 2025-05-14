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
     * Obtiene información del usuario autenticado a partir del token JWT.
     *
     * @param user Usuario extraído desde el contexto de seguridad.
     * @return Datos del usuario autenticado o 401 si no está autenticado.
     */
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal CustomOAuth2User user) {
        logger.info("Solicitud a /auth/me");

        if (user == null) {
            logger.warn("Usuario no autenticado al acceder a /me");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado");
        }

        User fullUser = authenticatedUserBl.getUserByEmail(user.getEmail());

        boolean profileComplete = fullUser.getBirthDate() != null &&
                fullUser.getCiNumber() != null &&
                fullUser.getAddress() != null;

        Map<String, Object> response = new HashMap<>();
        response.put("email", fullUser.getEmail());
        response.put("firstName", fullUser.getFirstName());
        response.put("lastName", fullUser.getLastName());
        response.put("userId", fullUser.getUserId());
        response.put("profileComplete", profileComplete);

        return ResponseEntity.ok(response);
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
     * @param authUser  Usuario autenticado (extraído del token OAuth2).
     * @param profileDto DTO con los campos que debe completar.
     * @return Respuesta 200 si se actualiza correctamente, 500 en caso de error.
     */
    @PutMapping("/complete-profile")
    public ResponseEntity<String> completeProfile(
            @AuthenticationPrincipal CustomOAuth2User authUser,
            @RequestBody CompleteProfileRequestDto profileDto
    ) {
        try {
            authenticatedUserBl.completeUserProfile(authUser.getEmail(), profileDto);
            return ResponseEntity.ok("Perfil completado correctamente");
        } catch (Exception e) {
            logger.error("Error al completar perfil", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al completar el perfil");
        }
    }

}