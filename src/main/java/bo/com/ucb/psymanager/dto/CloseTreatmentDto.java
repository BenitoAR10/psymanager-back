package bo.com.ucb.psymanager.dto;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CloseTreatmentDto {
    private Long id;
    private Long treatmentId;
    private String reason;
    private LocalDate closingDate;
}