package bo.com.ucb.psymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClosedTreatmentSummaryDto {

    private Long treatmentId;
    private String studentName;
    private LocalDate startDate;
    private LocalDate closingDate;
    private String reason;
    private Integer completedSessions;

    private Boolean wasReopened;
    private LocalDate reopeningDate;
}
