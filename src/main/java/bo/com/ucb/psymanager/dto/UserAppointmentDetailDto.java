package bo.com.ucb.psymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAppointmentDetailDto {
    private Long sessionId;
    private String therapistName;
    private String date;      // Formato: yyyy-MM-dd
    private String startTime; // Formato: HH:mm
    private String endTime;   // Formato: HH:mm
}
