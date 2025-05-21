package bo.com.ucb.psymanager.dto;

import lombok.Data;

@Data
public class CancelSessionRequestDto {

    /**
     * ID de la sesión programada que el paciente desea cancelar.
     */
    private Long scheduleSessionId;

    /**
     * Motivo de la cancelación proporcionado por el paciente.
     */
    private String cancellationReason;
}