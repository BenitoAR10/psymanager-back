package bo.com.ucb.psymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO  para exponer el conteo total de ejercicios completados
 */
@Data
@AllArgsConstructor
public class TotalCountResponseDto {

    /**
     * Numero total de ejercicios completado por el paciente en un periodo
     */

    private Long total;
}
