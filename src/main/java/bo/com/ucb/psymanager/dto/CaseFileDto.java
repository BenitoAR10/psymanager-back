package bo.com.ucb.psymanager.dto;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaseFileDto {
    private Long caseFileId;
    private Long treatmentId;
    private LocalDate date;
    private String summary;
    private String recommendations;
}
