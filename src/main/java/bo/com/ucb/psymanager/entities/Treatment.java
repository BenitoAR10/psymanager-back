package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un plan de tratamiento psicológico asignado a un paciente,
 * creado por un terapeuta. Contiene información contextual como fechas,
 * semestre académico, motivo, y la relación con sesiones del tratamiento.
 */
@Entity
@Table(name = "ps_treatment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Treatment {

    /** Identificador único del tratamiento */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treatment_id")
    private Long treatmentId;

    /** Enlace opcional a un tratamiento anterior (histórico o previo) */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_treatment_id", unique = true)
    private Treatment previousTreatment;


    /** Terapeuta responsable de este tratamiento */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_therapist_id", nullable = false)
    private UserTherapist userTherapist;

    /** Paciente al que pertenece este tratamiento */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_patient_id", nullable = false)
    private UserPatient userPatient;

    /** Fecha de inicio del tratamiento */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /** Fecha estimada o definida de finalización del tratamiento */
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    /** Motivo o descripción general del tratamiento */
    @Column(length = 100)
    private String reason;

    /** Semestre académico en el que se desarrolla el tratamiento */
    @Column(length = 100)
    private String semester;

    /** Lista de sesiones asociadas al tratamiento */
    @OneToMany(
            mappedBy = "treatment",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TreatmentSession> sessions = new ArrayList<>();

    @OneToOne(mappedBy = "previousTreatment")
    private Treatment reopenedTreatment;
}
