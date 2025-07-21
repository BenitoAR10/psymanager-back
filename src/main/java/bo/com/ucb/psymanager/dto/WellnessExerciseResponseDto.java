package bo.com.ucb.psymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO para exponer los datos de un ejercicio de bienestar al frontend.
 */
@Data
@AllArgsConstructor
public class WellnessExerciseResponseDto {
    private Long id;
    private String title;
    private String category;
    private Integer pointsReward;
    /** ¿Mostrar puntos al completar? */
    private Boolean showPoints;
    /**
     * URL pública del archivo de audio, accesible directamente desde frontend.
     */
    private String audioUrl;

}