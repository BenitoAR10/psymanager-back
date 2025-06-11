package bo.com.ucb.psymanager.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailyCountResponseDto {
    /**
     * Fecha (día) de la agregación.
     */
    private LocalDate day;
    /**
     * Cantidad de ejercicios completados ese día.
     */
    private Long count;
}