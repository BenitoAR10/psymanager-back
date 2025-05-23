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
    private String therapistPhoneNumber;
    private String date;
    private String startTime;
    private String endTime;
}
