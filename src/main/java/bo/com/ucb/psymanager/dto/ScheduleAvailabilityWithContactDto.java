package bo.com.ucb.psymanager.dto;

import bo.com.ucb.psymanager.entities.SessionState;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleAvailabilityWithContactDto {
    private Long scheduleId;
    private int therapistId;
    private LocalDate date;
    private String startTime;
    private String endTime;
    private String availabilityStatus;
    private String therapistName;
    private Long reservedByUserId;
    private SessionState sessionState;
    private String therapistPhoneNumber;
    private String therapistEmail;
}
