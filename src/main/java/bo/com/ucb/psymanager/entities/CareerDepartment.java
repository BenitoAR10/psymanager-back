package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Representa la información académica del paciente, como su carrera y facultad.
 * Se relaciona directamente con la entidad UserPatient.
 */
@Entity
@Table(name = "ps_career_department")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerDepartment {

    /** ID único generado automáticamente para cada carrera registrada */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "career_department_id")
    private Long careerDepartmentId;

    /** Nombre de la carrera (ej. Psicología) */
    @Column(name = "career_name", nullable = false, length = 100)
    private String careerName;

    /** Facultad a la que pertenece la carrera (ej. Ciencias Sociales) */
    @Column(name = "faculty", nullable = false, length = 100)
    private String faculty;

    /** Estado de la carrera (ej. Activo, Graduado, Retirado) */
    @Column(name = "status", nullable = false, length = 50)
    private String status;

    /** Relación muchos-a-uno con el paciente correspondiente */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_patient_id", nullable = false)
    private UserPatient userPatient;
}