package bo.com.ucb.psymanager.dto;

import lombok.Data;

/**
 * DTO utilizado para registrar la finalización de un ejercicio
 * por parte de un paciente autenticado.
 */
@Data
public class CompleteExerciseRequestDto {
    private Long exerciseId;
}
