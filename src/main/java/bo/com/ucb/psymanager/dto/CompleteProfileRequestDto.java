package bo.com.ucb.psymanager.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CompleteProfileRequestDto {
    private String phoneNumber;
    private Date birthDate;
    private String birthGender;
    private String identityGender;
    private String address;
    private String ciNumber;
    private String ciComplement;
    private String ciExtension;
}
