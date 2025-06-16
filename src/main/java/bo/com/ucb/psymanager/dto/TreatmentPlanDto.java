package bo.com.ucb.psymanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Representa un plan de tratamiento creado o consultado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentPlanDto {
    private Long id;
    private Long patientId;
    private Long therapistId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime firstSessionDateTime;

    private boolean recurrent;
    private Integer numberOfSessions;
    private Integer intervalWeeks;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String reason;
    private String semester;

    private List<TreatmentSessionDto> sessions; // opcional, para brindar todas las citas del plan

    private Long previousTreatmentId;
    private LocalDate previousEndDate;
    private String previousClosureReason;
}