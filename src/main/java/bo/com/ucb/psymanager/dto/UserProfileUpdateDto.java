package bo.com.ucb.psymanager.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateDto {
    private String phoneNumber;
    private String address;
    private String identityGender;
}