package bo.com.ucb.psymanager.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Representa cada sesión dentro de un plan de tratamiento.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentSessionDto {
    private Long id;
    private Long treatmentPlanId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime scheduledDateTime;

    private String state; // PENDING, ACCEPTED, REJECTED...
}