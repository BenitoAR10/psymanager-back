package bo.com.ucb.psymanager.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTreatmentPlanRequestDto {
    private Long patientId;

    private Long therapistId;

    private LocalDateTime firstSessionDateTime;

    /** Si es verdadero, se crearán las sesiones siguientes; si no, solo la primera. */
    private boolean recurrent;

    /** Número total de sesiones (incluida la primera). Solo relevante si recurrent=true */
    private Integer numberOfSessions;

    /** Intervalo en semanas entre cada sesión. Solo relevante si recurrent=true */
    private Integer intervalWeeks;

    /** Razoes para el tratamiento */
    private String reason;

    /** Semestre del tratamiento */
    private String semester;
}