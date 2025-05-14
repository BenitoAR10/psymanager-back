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
public class ActiveTreatmentStudentDto {
    private Long treatmentId;
    private Long patientId;
    private String studentName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalSessions;
    private Integer completedSessions;
}