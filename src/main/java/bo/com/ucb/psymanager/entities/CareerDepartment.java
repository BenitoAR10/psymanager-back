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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "career_department_id")
    private Long careerDepartmentId;

    /** Relación con el paciente */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_patient_id", nullable = false)
    private UserPatient userPatient;

    /** Nueva relación con la carrera */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_id", nullable = false)
    private Career career;
}
