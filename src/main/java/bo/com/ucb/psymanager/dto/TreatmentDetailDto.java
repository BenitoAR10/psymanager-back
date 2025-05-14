package bo.com.ucb.psymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentDetailDto {
    private Long treatmentId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String semester;
    private List<TreatmentSessionDetailDto> sessions;
}