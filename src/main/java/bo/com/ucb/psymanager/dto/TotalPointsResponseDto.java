package bo.com.ucb.psymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO de respuesta para el total de puntos de un paciente.
 */
@Data
@AllArgsConstructor
public class TotalPointsResponseDto {
    /** Total de puntos acumulados por el paciente */
    private int totalPoints;
}