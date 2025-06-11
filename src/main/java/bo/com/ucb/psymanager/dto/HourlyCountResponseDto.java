package bo.com.ucb.psymanager.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class HourlyCountResponseDto {
    /**
     * Fecha y hora truncada a la hora (p. ej. 2025-06-07T15:00).
     */
    private LocalDateTime hour;
    /**
     * Cantidad de ejercicios completados en esa hora.
     */
    private Long count;
}