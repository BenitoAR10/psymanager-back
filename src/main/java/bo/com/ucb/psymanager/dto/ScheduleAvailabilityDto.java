package bo.com.ucb.psymanager.dto;

import bo.com.ucb.psymanager.entities.SessionState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleAvailabilityDto {
    private Long scheduleId;
    private int therapistId;
    private LocalDate date;
    private String startTime;
    private String endTime;
    private String availabilityStatus; // "available" or "taken"
    private String therapistName;
    private Long reservedByUserId; // ID del usuario que reservó la cita
    private SessionState sessionState;
}
