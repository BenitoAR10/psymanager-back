package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 * Representa a un usuario registrado en el sistema, incluyendo datos personales y de contacto.
 * Esta entidad es la base para pacientes, terapeutas y administradores.
 */
@Data
@Entity
@Table(name = "ps_user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /** Identificador único del usuario */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    /** Nombre(s) del usuario */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /** Apellido paterno */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /** Apellido materno (opcional) */
    @Column(name = "mothers_last_name")
    private String mothersLastName;

    /** Email institucional del usuario, único en el sistema */
    @Column(nullable = false, unique = true)
    private String email;

    /** Número de teléfono (opcional, usado para notificaciones) */
    @Column(name = "phone_number")
    private String phoneNumber;

    /** Fecha de nacimiento del usuario */
    @Column(name = "birth_date")
    private Date birthDate;

    /** Género biológico al nacer */
    @Column(name = "birth_gender")
    private String birthGender;

    /** Género con el que se identifica el usuario */
    @Column(name = "identity_gender")
    private String identityGender;

    /** Dirección física (opcional) */
    @Column(name = "address")
    private String address;

    /** Número de carnet de identidad (único) */
    @Column(name = "ci_number", unique = true)
    private String ciNumber;

    /** Complemento del CI (opcional) */
    @Column(name = "ci_complement")
    private String ciComplement;

    /** Extensión del CI (por ejemplo: LP, CB, SCZ) */
    @Column(name = "ci_extension")
    private String ciExtension;
}
