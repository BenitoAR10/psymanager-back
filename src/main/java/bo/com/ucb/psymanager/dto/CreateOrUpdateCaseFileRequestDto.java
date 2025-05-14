package bo.com.ucb.psymanager.dto;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateCaseFileRequestDto {
    private Long treatmentId;
    private LocalDate date;
    private String summary;
    private String recommendations;
}