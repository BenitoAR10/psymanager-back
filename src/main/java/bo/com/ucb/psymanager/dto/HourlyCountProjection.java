package bo.com.ucb.psymanager.dto;

import java.time.LocalDateTime;

public interface HourlyCountProjection {
    LocalDateTime getHour();
    Long getCount();
}