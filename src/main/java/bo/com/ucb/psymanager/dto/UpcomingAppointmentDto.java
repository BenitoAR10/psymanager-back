package bo.com.ucb.psymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpcomingAppointmentDto {
    private Long patientId;
    private Long therapistId;
    private Long appointmentId;
    private String studentName;
    private LocalDateTime dateTime;
    private String state;
    private Boolean isPartOfTreatment;
    private String reason;
}
