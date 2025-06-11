package bo.com.ucb.psymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HourlySeriesResponseDto {
    private Long total;
    private List<HourlyCountResponseDto> series;
}
