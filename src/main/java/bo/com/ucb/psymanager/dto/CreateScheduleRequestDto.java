package bo.com.ucb.psymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateScheduleRequestDto {
    private int userTherapistId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}
