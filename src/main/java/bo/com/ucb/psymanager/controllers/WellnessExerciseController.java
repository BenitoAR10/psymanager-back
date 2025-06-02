package bo.com.ucb.psymanager.controllers;

import bo.com.ucb.psymanager.bl.AuthenticatedUserBl;
import bo.com.ucb.psymanager.bl.PermissionBl;
import bo.com.ucb.psymanager.bl.WellnessExerciseBl;
import bo.com.ucb.psymanager.dto.CreateWellnessExerciseDto;
import bo.com.ucb.psymanager.dto.WellnessExerciseResponseDto;
import bo.com.ucb.psymanager.entities.User;
import bo.com.ucb.psymanager.entities.WellnessExercise;
import bo.com.ucb.psymanager.service.MinioService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controlador para operaciones relacionadas con ejercicios de bienestar.
 */
@RestController
@RequestMapping("/api/wellness-exercises")
@RequiredArgsConstructor
@Slf4j
public class WellnessExerciseController {

    private final MinioService minioService;
    private final WellnessExerciseBl wellnessExerciseBl;
    private final AuthenticatedUserBl authenticatedUserBl;
    private final PermissionBl permissionBl;



    /**
     * Endpoint para subir un archivo de audio al bucket de MinIO.
     *
     * @param file Archivo de audio a subir
     * @param objectName Nombre con el que se almacenará en MinIO (ej. "calma-diaria.mp3")
     * @return Mensaje de éxito o error
     */
    @PostMapping("/upload-audio")
    @PreAuthorize("hasRole('THERAPIST')")
    public ResponseEntity<String> uploadAudio(
            @RequestParam("file") MultipartFile file,
            @RequestParam("objectName") String objectName
    ) {
        minioService.uploadExerciseAudio(file, objectName);
        return ResponseEntity.ok("Archivo subido correctamente: " + objectName);
    }

    /**
     * Endpoint para registrar un nuevo ejercicio de bienestar emocional.
     * Solo accesible si el terapeuta tiene el permiso ADD_EXERCISE_RESOURCE.
     *
     * @param dto DTO con datos del ejercicio (título, categoría, puntos, archivo)
     * @param authHeader Encabezado Authorization con el token JWT
     * @return DTO del ejercicio creado con URL firmada
     */
    @PostMapping
    public ResponseEntity<WellnessExerciseResponseDto> createExercise(
            @ModelAttribute CreateWellnessExerciseDto dto,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        // 1. Validar encabezado y extraer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Intento de acceso sin token válido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authHeader.substring(7);

        // 2. Autenticar usuario
        User user = authenticatedUserBl.getAuthenticatedUser(token)
                .orElseThrow(() -> {
                    log.warn("Token inválido o usuario no encontrado");
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
                });

        // 3. Validar permiso
        if (!permissionBl.hasPermission(user, "ADD_EXERCISE_RESOURCE")) {
            log.warn("Usuario {} sin permiso ADD_EXERCISE_RESOURCE", user.getUserId());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // 4. Crear ejercicio
        WellnessExercise created = wellnessExerciseBl.createExercise(dto);
        String publicUrl = minioService.getPresignedUrl(created.getAudioUrl());

        // 5. Construir respuesta
        WellnessExerciseResponseDto responseDto = new WellnessExerciseResponseDto(
                created.getId(),
                created.getTitle(),
                created.getCategory(),
                created.getPointsReward(),
                publicUrl
        );

        log.info("Ejercicio creado por el usuario {}: {}", user.getUserId(), created.getId());
        return ResponseEntity.ok(responseDto);
    }



    /**
     * Endpoint para obtener la lista de ejercicios filtrados por categoría.
     * Si no se especifica ninguna categoría, se devuelven todos los ejercicios.
     *
     * @param category Categoría opcional (por ejemplo: "Ansiedad", "Estrés")
     * @return Lista de ejercicios con sus respectivos datos y URL firmada de audio.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<WellnessExerciseResponseDto>> getExercises(
            @RequestParam(value = "category", required = false) String category
    ) {
        List<WellnessExerciseResponseDto> response = wellnessExerciseBl.getExercisesByCategory(category);
        return ResponseEntity.ok(response);
    }


}