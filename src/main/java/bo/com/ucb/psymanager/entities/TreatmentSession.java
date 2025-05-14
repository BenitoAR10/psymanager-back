package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa una sesión individual dentro de un plan de tratamiento.
 * Cada sesión está vinculada a una sesión agendada concreta y puede tener notas.
 */
@Entity
@Table(name = "ps_treatment_session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentSession {

    /** ID único de la sesión dentro del tratamiento */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treatment_session_id")
    private Long treatmentSessionId;

    /** Plan de tratamiento al que pertenece esta sesión */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "treatment_id", nullable = false)
    private Treatment treatment;

    /** Sesión programada concreta en el calendario */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_session_id", nullable = false)
    private ScheduleSession scheduleSession;

    /** Orden de esta sesión dentro del tratamiento (1, 2, 3, ...) */
    @Column(name = "session_order", nullable = false)
    private Integer sessionOrder;

    /** Indicador de si esta sesión ya fue completada */
    @Column(name = "completed", nullable = false)
    private Boolean completed = false;

    /** Nota asociada a esta sesión, registrada por el terapeuta */
    @OneToOne(mappedBy = "treatmentSession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SessionNote notes;
}
