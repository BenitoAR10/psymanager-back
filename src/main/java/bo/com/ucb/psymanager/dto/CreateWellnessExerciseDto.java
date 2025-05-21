package bo.com.ucb.psymanager.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO utilizado para crear un nuevo ejercicio de bienestar emocional.
 * Incluye los metadatos del ejercicio y el archivo de audio asociado.
 */
@Data
public class CreateWellnessExerciseDto {

    private String title;

    private String category;

    private Integer pointsReward;

    /** Archivo de audio del ejercicio, en formato .mp3 o .wav */
    private MultipartFile audioFile;
}