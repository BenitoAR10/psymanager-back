package bo.com.ucb.psymanager.controllers;

import bo.com.ucb.psymanager.bl.ScheduledSessionBl;
import bo.com.ucb.psymanager.bl.TreatmentSessionBl;
import bo.com.ucb.psymanager.dao.UserDao;
import bo.com.ucb.psymanager.dto.*;
import bo.com.ucb.psymanager.entities.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST para gestionar sesiones programadas (agendamiento, aceptación y visualización).
 */
@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8081")
@PreAuthorize("hasAnyRole('PATIENT', 'THERAPIST')")
public class ScheduledSessionController {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledSessionController.class);

    private final ScheduledSessionBl scheduledSessionBl;
    private final TreatmentSessionBl treatmentSessionBl;
    private final UserDao userDao;

    /**
     * Registra una solicitud de cita para un horario específico.
     *
     * @param request DTO con el ID del horario solicitado
     * @param email email del usuario autenticado
     * @return mensaje de confirmación
     */
    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping
    public ResponseEntity<ApiResponseDto> createSession(
            @RequestBody CreateSessionRequestDto request,
            @AuthenticationPrincipal String email
    ) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Long userId = user.getUserId();
        logger.info("POST /api/sessions → usuario {} solicita horario {}", userId, request.getTherapistScheduledId());

        scheduledSessionBl.createScheduledSession(request.getTherapistScheduledId(), userId, request.getReason());

        return ResponseEntity.ok(new ApiResponseDto("Solicitud de cita registrada correctamente."));
    }

    /**
     * Permite al terapeuta aceptar o rechazar una sesión solicitada.
     *
     * @param sessionId ID de la sesión
     * @param request DTO con el nuevo estado (ACCEPTED o REJECTED)
     * @return mensaje de resultado
     */
    @PreAuthorize("hasRole('THERAPIST')")
    @PutMapping("/{sessionId}/state")
    public ResponseEntity<ApiResponseDto> updateSessionState(
            @PathVariable Long sessionId,
            @RequestBody UpdateSessionStateRequestDto request
    ) {
        logger.info("PUT /api/sessions/{}/state → nuevo estado: {}", sessionId, request.getNewState());

        scheduledSessionBl.updateSessionState(sessionId, request.getNewState());

        return ResponseEntity.ok(new ApiResponseDto("Estado de la sesión actualizado correctamente."));
    }

    /**
     * Marca una sesión individual (fuera de tratamiento) como completada.
     * Esta operación solo está disponible para sesiones que ya finalizaron y no están asociadas a un tratamiento.
     *
     * @param sessionId ID de la sesión a marcar como completada
     * @return Respuesta 200 OK si se completó exitosamente
     */
    @PreAuthorize("hasRole('THERAPIST')")
    @PutMapping("/{sessionId}/complete")
    public ResponseEntity<Void> markSessionAsCompleted(@PathVariable Long sessionId) {
        logger.info("Petición para marcar como COMPLETED la sesión ID={}", sessionId);

        scheduledSessionBl.markScheduleSessionAsCompleted(sessionId);

        return ResponseEntity.ok().build();
    }

    /**
     * Devuelve todas las sesiones individuales del terapeuta autenticado,
     * sin importar el estado o la fecha (pasadas, futuras, aceptadas, rechazadas, completadas).
     *
     * @param email email del terapeuta autenticado
     * @return lista completa de citas como DTOs
     */
    @PreAuthorize("hasRole('THERAPIST')")
    @GetMapping("/all")
    public ResponseEntity<List<UpcomingAppointmentDto>> getAllAppointmentsForTherapist(
            @AuthenticationPrincipal String email
    ) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<UpcomingAppointmentDto> allAppointments =
                scheduledSessionBl.getAllAppointmentsForTherapist(user.getUserId());

        return ResponseEntity.ok(allAppointments);
    }



    /**
     * Devuelve las próximas citas del terapeuta autenticado, ordenadas por fecha.
     *
     * @param email email del terapeuta autenticado
     * @param limit número máximo de resultados (por defecto 5)
     * @return lista de próximas citas
     */
    @PreAuthorize("hasRole('THERAPIST')")
    @GetMapping("/upcoming")
    public ResponseEntity<List<UpcomingAppointmentDto>> getUpcomingAppointments(
            @AuthenticationPrincipal String email,
            @RequestParam(defaultValue = "5") int limit
    ) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<UpcomingAppointmentDto> appointments =
                scheduledSessionBl.getUpcomingAppointmentsForTherapist(user.getUserId(), limit);

        return ResponseEntity.ok(appointments);
    }

    /**
     * Devuelve todas las solicitudes de cita en estado PENDING del terapeuta autenticado.
     *
     * @param email email del terapeuta autenticado
     * @return lista de solicitudes pendientes
     */
    @PreAuthorize("hasRole('THERAPIST')")
    @GetMapping(params = "state=PENDING")
    public ResponseEntity<List<UpcomingAppointmentDto>> getPendingAppointments(
            @AuthenticationPrincipal String email
    ) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<UpcomingAppointmentDto> pending =
                scheduledSessionBl.getPendingAppointmentsForTherapist(user.getUserId());

        return ResponseEntity.ok(pending);
    }

    /**
     * Devuelve todas las sesiones programadas dentro del tratamiento activo del paciente autenticado.
     *
     * @param email email del paciente autenticado
     * @return lista de sesiones agendadas como parte de tratamiento
     */
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/my/treatment-sessions")
    public ResponseEntity<List<ScheduleAvailabilityDto>> getSessionsFromActiveTreatment(
            @AuthenticationPrincipal String email
    ) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        logger.info("GET /api/sessions/my/treatment-sessions → paciente ID={}", user.getUserId());

        List<ScheduleAvailabilityDto> dtos =
                treatmentSessionBl.getScheduledSessionsFromActiveTreatment(user.getUserId());

        return ResponseEntity.ok(dtos);
    }
}
