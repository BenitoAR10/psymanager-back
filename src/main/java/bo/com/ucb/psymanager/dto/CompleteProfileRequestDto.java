package bo.com.ucb.psymanager.dto;

import lombok.*;

import java.util.Date;

/**
 * DTO para completar el perfil del paciente después del registro inicial.
 * Incluye información personal adicional y los datos académicos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompleteProfileRequestDto {

    private Date birthDate;
    private String birthGender;
    private String identityGender;
    private String phoneNumber;
    private String address;

    private String ciNumber;
    private String ciComplement;
    private String ciExtension;

    // Datos académicos
    private String careerName;
    private String faculty;
    private String status;
}