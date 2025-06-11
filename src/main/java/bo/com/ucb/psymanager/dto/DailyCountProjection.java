package bo.com.ucb.psymanager.dto;

import java.time.LocalDate;

public interface DailyCountProjection {
    LocalDate getDay();
    Long getCount();
}