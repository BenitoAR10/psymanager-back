package bo.com.ucb.psymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO para exponer el numero de ejercicios completados por semana
 */
@Data
@AllArgsConstructor
public class WeeklyCountResponseDto {
    /**
     * Año ISO de la semana
     */
    private int year;
    /**
     * Numero de la ene el año (ISO)
     */
    private int week;
    /**
     * Cantidad de ejercicios completado en esa semana
     */
    private Long count;
}
