package bo.com.ucb.psymanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleResponseDto {
    private Long therapistScheduleId;
    private int userTherapistId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String therapistName;

    // Constructor personalizado usando LocalDate para 'date' y LocalTime para start/end
    public ScheduleResponseDto(Long therapistScheduleId, int userTherapistId, LocalDate date, LocalTime startTime, LocalTime endTime, String therapistName) {
        this.therapistScheduleId = therapistScheduleId;
        this.userTherapistId = userTherapistId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.therapistName = therapistName;
    }
}