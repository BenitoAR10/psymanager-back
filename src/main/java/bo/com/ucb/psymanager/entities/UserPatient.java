package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa un paciente en el sistema. Extiende los datos básicos de la entidad User.
 * Usa clave primaria compartida con la tabla ps_user mediante @MapsId.
 */
@Entity
@Table(name = "ps_user_patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPatient {

    /** ID del paciente, que es también el ID del usuario base */
    @Id
    @Column(name = "user_patient_id")
    private Long userPatientId;

    /** Relación uno a uno con el usuario general del sistema */
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_patient_id")
    private User user;
}
