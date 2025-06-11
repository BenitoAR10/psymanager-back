package bo.com.ucb.psymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO para exponer el conteo de ejercicios completados, agrupado por catecogia
 */
@Data
@AllArgsConstructor
public class CategoryCountResponseDto {
    /**
     * Nombre de la categoria del ejercicio
     */
    private String category;
    /**
     * Numero de ejercicios completado en esa catagoria
     */
    private Long count;
}
