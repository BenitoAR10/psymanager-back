package bo.com.ucb.psymanager.dto;


import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Date birthDate;
    private String identityGender;
    private String address;
    private String ciNumber;
}