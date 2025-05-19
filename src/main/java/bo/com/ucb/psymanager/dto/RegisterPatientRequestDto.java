package bo.com.ucb.psymanager.dto;

import lombok.*;

/**
 * DTO para registrar a un nuevo paciente en el sistema.
 * Contiene los datos básicos iniciales para crear un usuario
 * y asignarle el rol de paciente.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPatientRequestDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

}