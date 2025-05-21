package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Representa una sesión programada entre un terapeuta y un paciente.
 * Puede ser una sesión suelta o parte de un tratamiento.
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

    /**
     * Estado de la sesión:
     * - PENDING: solicitud enviada por el paciente.
     * - ACCEPTED: confirmada por el terapeuta.
     * - REJECTED: rechazada por el terapeuta.
     * - CANCELED: cancelada por el paciente.
     * - COMPLETED: marcada como realizada por el terapeuta.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private SessionState state;

    /** Lista de sesiones de tratamiento asociadas (si aplica) */
    @OneToMany(mappedBy = "scheduleSession", fetch = FetchType.LAZY)
    private List<TreatmentSession> treatmentSessions;
}