package bo.com.ucb.psymanager.controllers;

import bo.com.ucb.psymanager.bl.WellnessExerciseBl;
import bo.com.ucb.psymanager.dto.CreateWellnessExerciseDto;
import bo.com.ucb.psymanager.dto.WellnessExerciseResponseDto;
import bo.com.ucb.psymanager.entities.WellnessExercise;
import bo.com.ucb.psymanager.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controlador para operaciones relacionadas con ejercicios de bienestar.
 */
@RestController
@RequestMapping("/api/wellness-exercises")
@RequiredArgsConstructor
public class WellnessExerciseController {

    private final MinioService minioService;
    private final WellnessExerciseBl wellnessExerciseBl;

    /**
     * Endpoint para subir un archivo de audio al bucket de MinIO.
     *
     * @param file Archivo de audio a subir
     * @param objectName Nombre con el que se almacenará en MinIO (ej. "calma-diaria.mp3")
     * @return Mensaje de éxito o error
     */
    @PostMapping("/upload-audio")
    public ResponseEntity<String> uploadAudio(
            @RequestParam("file") MultipartFile file,
            @RequestParam("objectName") String objectName
    ) {
        minioService.uploadExerciseAudio(file, objectName);
        return ResponseEntity.ok("Archivo subido correctamente: " + objectName);
    }

    /**
     * Endpoint para registrar un nuevo ejercicio de bienestar emocional.
     * Recibe el DTO con el archivo incluido.
     *
     * @param dto DTO que contiene título, categoría, puntos y audio
     * @return Ejercicio guardado
     */
    @PostMapping
    public ResponseEntity<WellnessExercise> createExercise(@ModelAttribute CreateWellnessExerciseDto dto) {
        WellnessExercise created = wellnessExerciseBl.createExercise(dto);
        return ResponseEntity.ok(created);
    }

    /**
     * Endpoint para obtener la lista de ejercicios filtrados por categoría.
     * Si no se especifica ninguna categoría, se devuelven todos los ejercicios.
     *
     * @param category Categoría opcional (por ejemplo: "Ansiedad", "Estrés")
     * @return Lista de ejercicios con sus respectivos datos y URL firmada de audio.
     */
    @GetMapping
    public ResponseEntity<List<WellnessExerciseResponseDto>> getExercises(
            @RequestParam(value = "category", required = false) String category
    ) {
        List<WellnessExerciseResponseDto> response = wellnessExerciseBl.getExercisesByCategory(category);
        return ResponseEntity.ok(response);
    }


}