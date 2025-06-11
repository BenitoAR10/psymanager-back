package bo.com.ucb.psymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DailySeriesResponseDto {
    private Long total;
    private List<DailyCountResponseDto> series;
}