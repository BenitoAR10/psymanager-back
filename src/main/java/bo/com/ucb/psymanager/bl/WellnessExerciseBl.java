package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.WellnessExerciseDao;
import bo.com.ucb.psymanager.dto.CreateWellnessExerciseDto;
import bo.com.ucb.psymanager.dto.WellnessExerciseResponseDto;
import bo.com.ucb.psymanager.entities.WellnessExercise;
import bo.com.ucb.psymanager.service.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Lógica de negocio relacionada con los ejercicios de bienestar emocional.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WellnessExerciseBl {

    @Value("${minio.public-host}")
    private String minioPublicHost;


    private final WellnessExerciseDao wellnessExerciseDao;
    private final MinioService minioService;

    /**
     * Crea un nuevo ejercicio de bienestar, subiendo el audio a MinIO
     * y registrando los metadatos en la base de datos.
     *
     * @param dto Datos del ejercicio y archivo de audio
     * @return El ejercicio guardado
     */
    public WellnessExercise createExercise(CreateWellnessExerciseDto dto) {
        MultipartFile file = dto.getAudioFile();

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo de audio no puede estar vacío.");
        }

        try {
            // Subir a MinIO con el nombre original del archivo
            String objectName = file.getOriginalFilename();
            minioService.uploadExerciseAudio(file, objectName);

            // Crear y guardar el ejercicio
            WellnessExercise exercise = new WellnessExercise();
            exercise.setTitle(dto.getTitle());
            exercise.setCategory(dto.getCategory());
            exercise.setPointsReward(dto.getPointsReward());
            exercise.setAudioUrl(objectName); // Guardamos solo el nombre del archivo

            return wellnessExerciseDao.save(exercise);

        } catch (Exception e) {
            log.error("Error al crear ejercicio de bienestar", e);
            throw new RuntimeException("No se pudo registrar el ejercicio de bienestar.");
        }
    }

    /**
     * Obtiene todos los ejercicios de bienestar registrados en el sistema
     * y genera una URL firmada para acceder al audio desde MinIO.
     *
     * @return Lista de ejercicios listos para ser mostrados en frontend.
     */
    public List<WellnessExerciseResponseDto> getAllExercises() {
        return wellnessExerciseDao.findAll().stream()
                .map(ex -> new WellnessExerciseResponseDto(
                        ex.getId(),
                        ex.getTitle(),
                        ex.getCategory(),
                        ex.getPointsReward(),
                        minioPublicHost + "/wellness-audios/" + ex.getAudioUrl()



                ))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene la lista de ejercicios filtrados por categoría si se proporciona.
     * Si no se especifica ninguna categoría, devuelve todos.
     *
     * @param category Categoría opcional para filtrar (por ejemplo: "Estrés")
     * @return Lista de ejercicios con URLs firmadas
     */
    public List<WellnessExerciseResponseDto> getExercisesByCategory(String category) {
        List<WellnessExercise> exercises;

        if (StringUtils.hasText(category)) {
            exercises = wellnessExerciseDao.findByCategory(category);
        } else {
            exercises = wellnessExerciseDao.findAll();
        }

        return exercises.stream()
                .map(ex -> new WellnessExerciseResponseDto(
                        ex.getId(),
                        ex.getTitle(),
                        ex.getCategory(),
                        ex.getPointsReward(),
                        minioPublicHost + "/wellness-audios/" + ex.getAudioUrl()


                ))
                .collect(Collectors.toList());
    }
}