package bo.com.ucb.psymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentSessionDetailDto {
    private Long sessionId;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String state; // ACCEPTED / PENDING / REJECTED
    private Boolean completed;
    private String notes;
    private Integer sessionOrder;
}