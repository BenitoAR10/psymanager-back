package bo.com.ucb.psymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDetailDto {
    private Long scheduleId;
    private String therapistName;
    private String date;
    private List<ScheduleAvailabilityDto> availableTimes;
}
