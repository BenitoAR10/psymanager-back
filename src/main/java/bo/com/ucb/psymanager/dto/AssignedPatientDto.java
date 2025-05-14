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
public class AssignedPatientDto {
    private Long treatmentId;
    private Long patientId;
    private String studentName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int assignedSessions;
    private int completedSessions;
    private boolean active;
}
