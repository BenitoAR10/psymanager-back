package bo.com.ucb.psymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAppointmentDto {
   private Long sessionId;
   private String therapistName;
   private String date;
   private String startTime;
   private String endTime;
}
