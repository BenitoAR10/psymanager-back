package bo.com.ucb.psymanager.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClosedTreatmentDetailDto {

    private Long treatmentId;
    private String studentName;
    private String semester;
    private String reason;
    private LocalDate startDate;
    private LocalDate endDate;
    private String closureReason;
    private LocalDate closingDate;
    private CaseFileDto caseFile;
    private List<SessionNoteSummaryDto> sessionNotes;
}
