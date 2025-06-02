package bo.com.ucb.psymanager.controllers;

import bo.com.ucb.psymanager.bl.AuthenticatedUserBl;
import bo.com.ucb.psymanager.bl.ManualUserRegistrationBl;
import bo.com.ucb.psymanager.dto.AuthResponseDto;
import bo.com.ucb.psymanager.dto.CompleteProfileRequestDto;
import bo.com.ucb.psymanager.dto.LoginRequestDto;
import bo.com.ucb.psymanager.dto.RegisterPatientRequestDto;
import bo.com.ucb.psymanager.entities.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    private final ManualUserRegistrationBl manualUserRegistrationBl;




    @Autowired
    public AuthController(AuthenticatedUserBl authenticatedUserBl, ManualUserRegistrationBl manualUserRegistrationBl) {
        this.authenticatedUserBl = authenticatedUserBl;
        this.manualUserRegistrationBl = manualUserRegistrationBl;
    }

    /**
     * Obtiene la información del usuario autenticado a partir del token JWT.
     * Este endpoint es utilizado para validar la sesión del usuario y verificar
     * si su perfil está completo.
     *
     * @param email Email del usuario autenticado, extraído del token JWT por Spring Security.
     * @return Un JSON con los datos básicos del usuario y el estado de su perfil.
     */
    @PreAuthorize("isAuthenticated()")
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
     * Endpoint para iniciar sesión con email y contraseña.
     *
     * @param dto Objeto que contiene las credenciales de inicio de sesión.
     * @return ResponseEntity con los tokens JWT si la autenticación es exitosa.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto dto) {
        logger.info("Solicitud de inicio de sesión recibida para: " + dto.getEmail());
        AuthResponseDto response = authenticatedUserBl.loginWithEmailAndPassword(dto);
        logger.info("Login exitoso para: " + dto.getEmail());
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para registrar a un nuevo paciente con email y contraseña.
     *
     * @param dto DTO con los datos básicos requeridos para el registro.
     * @return ResponseEntity con código 201 (Created) si el registro es exitoso.
     */
    @PostMapping("/register")
    public ResponseEntity<Void> registerNewPatient(@RequestBody RegisterPatientRequestDto dto) {
        logger.info("Solicitud de registro recibida para: " + dto.getEmail());
        manualUserRegistrationBl.registerNewPatientUser(dto);
        logger.info("Registro exitoso para: " + dto.getEmail());
        return ResponseEntity.status(201).build();
    }



    /**
     * Permite que el usuario autenticado complete su información personal y académica.
     * Email del usuario autenticado extraído del token JWT.
     * @param profileDto DTO con los campos personales y académicos a completar.
     * @return Respuesta 200 si se actualiza correctamente, 500 en caso de error.
     */
    @PutMapping("/complete-profile")
    public ResponseEntity<Map<String, String>> completeProfile(
            Authentication authentication,
            @RequestBody CompleteProfileRequestDto profileDto
    ) {
        String email = authentication.getName();
        try {
            logger.info("Solicitud para completar perfil del usuario: " + email);
            manualUserRegistrationBl.completePatientProfile(email, profileDto);
            logger.info("Perfil completado correctamente para: " + email);
            return ResponseEntity.ok(Map.of("message", "Perfil completado correctamente"));
        } catch (Exception e) {
            logger.error("Error al completar perfil para: " + email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error al completar el perfil"));
        }
    }

}