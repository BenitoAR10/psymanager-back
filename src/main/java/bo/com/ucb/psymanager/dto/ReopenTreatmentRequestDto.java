package bo.com.ucb.psymanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;


/**
 * DTO para solicitar la reapertura de un tratamiento previamente cerrado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReopenTreatmentRequestDto {

    /**
     * ID del tratamiento previamente cerrado que se desea retomar.
     */
    private Long previousTreatmentId;

    /**
     * ID del terapeuta que reabre el tratamiento.
     */
    private Long therapistId;


    /**
     * Fecha de inicio del nuevo tratamiento.
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate newStartDate;

    /**
     * Fecha estimada o definida de finalización del nuevo tratamiento.
     * En esta versión simplificada será igual a la fecha de inicio.
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate newEndDate;

    /**
     * Motivo del nuevo tratamiento, puede ser heredado o ajustado por el terapeuta.
     */
    private String reason;

    /**
     * Semestre académico en el que se desarrollará el nuevo tratamiento.
     */
    private String semester;
}
