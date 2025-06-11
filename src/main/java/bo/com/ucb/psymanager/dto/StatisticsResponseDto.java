package bo.com.ucb.psymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * DTO general que agrupa las tres metricas de estadisticas
 * total, serie semanal y distribucion por categoria
 */
@Data
@AllArgsConstructor
public class StatisticsResponseDto {
    /**
     * Conteo total de ejericicos completados
     */
    private TotalCountResponseDto total;
    /**
     * Serie semanal de conteo de ejercicios (año + semana)
     */
    private List<WeeklyCountResponseDto> series;
    /**
     * Conteo de ejercicios completado por categoria
     */
    private List<CategoryCountResponseDto> byCategory;
}
