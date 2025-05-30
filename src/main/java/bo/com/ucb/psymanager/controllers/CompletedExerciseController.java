package bo.com.ucb.psymanager.controllers;

import bo.com.ucb.psymanager.bl.CompletedExerciseBl;
import bo.com.ucb.psymanager.dto.CompleteExerciseRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para registrar ejercicios de bienestar completados
 * y asignar puntos al paciente autenticado.
 */
@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
@Slf4j
public class CompletedExerciseController {

    private final CompletedExerciseBl completedExerciseBl;

    /**
     * Endpoint para registrar que el paciente autenticado completó un ejercicio.
     * Actualiza el puntaje acumulado del paciente.
     *
     * @param dto     DTO con el ID del ejercicio completado.
     * @param email   Email del paciente autenticado (inyectado desde el token).
     * @return 200 OK con cuerpo JSON simple.
     */
    @PostMapping("/complete")
    public ResponseEntity<Map<String, Object>> completeExercise(
            @RequestBody CompleteExerciseRequestDto dto,
            @AuthenticationPrincipal String email
    ) {
        completedExerciseBl.completeExercise(email, dto);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Ejercicio registrado con éxito");
        response.put("exerciseId", dto.getExerciseId());
        return ResponseEntity.ok(response);
    }



}