package bo.com.ucb.psymanager.controllers;

import bo.com.ucb.psymanager.bl.UserAppointmentBl;
import bo.com.ucb.psymanager.dao.UserDao;
import bo.com.ucb.psymanager.dto.UserAppointmentDetailDto;
import bo.com.ucb.psymanager.dto.UserAppointmentDto;
import bo.com.ucb.psymanager.entities.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions/my")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8081")
public class UserAppointmentController {
    private static final Logger logger = LoggerFactory.getLogger(UserAppointmentController.class);

    private final UserAppointmentBl userAppointmentBl;
    private final UserDao userDao;

    /**
     * Obtiene todas las citas agendadas por el usuario autenticado.
     *
     * @param email Email extraído del token JWT
     * @return Lista de citas agendadas
     */
    @GetMapping
    public ResponseEntity<List<UserAppointmentDto>> getMyAppointments(
            @AuthenticationPrincipal String email
    ) {
        logger.info("Solicitud de citas agendadas por el usuario con email: {}", email);

        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Long userId = user.getUserId();

        List<UserAppointmentDto> appointments = userAppointmentBl.getUserAppointments(userId);

        return ResponseEntity.ok(appointments);
    }
    /**
     * Obtiene los detalles de una cita específica del usuario autenticado.
     *
     * @param sessionId ID de la sesión agendada
     * @param email Email extraído del token JWT
     * @return Detalles de la cita si pertenece al usuario
     */
    @GetMapping("/{sessionId}")
    public ResponseEntity<UserAppointmentDetailDto> getMyAppointmentDetail(
            @PathVariable Long sessionId,
            @AuthenticationPrincipal String email
    ) {
        logger.info("Solicitud de detalle de cita con ID {} por el usuario con email: {}", sessionId, email);

        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return userAppointmentBl.getAppointmentDetail(sessionId, user.getUserId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
