package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Representa una sesión programada entre un terapeuta y un paciente,
 * derivada de un bloque de horario definido previamente por el terapeuta.
 */
@Entity
@Table(name = "ps_schedule_session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleSession {

    /** Identificador único de la sesión programada */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_session_id")
    private Long scheduleSessionId;

    /** Bloque de horario definido por el terapeuta en el que se agendó esta sesión */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "therapist_scheduled_id", nullable = false)
    private TherapistScheduled therapistScheduled;

    /** Paciente asignado a esta sesión */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_patient_id", nullable = false)
    private UserPatient userPatient;

    /** Estado de la sesión (ej. ACCEPTED, PENDING, AVAILABLE) */
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private SessionState state;

    /** Lista de sesiones de tratamiento que hacen referencia a esta sesión programada */
    @OneToMany(mappedBy = "scheduleSession", fetch = FetchType.LAZY)
    private List<TreatmentSession> treatmentSessions;
}
