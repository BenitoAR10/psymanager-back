package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Representa un bloque de disponibilidad definido por un terapeuta en una fecha y hora específicas.
 * Estas franjas horarias son las que los pacientes pueden reservar para agendar sesiones.
 */
@Entity
@Table(name = "ps_therapist_scheduled")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TherapistScheduled {

    /** Identificador único del bloque de horario del terapeuta */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "therapist_scheduled_id")
    private Long therapistScheduledId;

    /** ID del terapeuta que definió este horario (referencia a UserTherapist) */
    @Column(name = "user_therapist_id", nullable = false)
    private int userTherapistId;

    /** Fecha del bloque de disponibilidad */
    @Column(name = "date", nullable = false)
    private LocalDate date;

    /** Hora de inicio del bloque */
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    /** Hora de fin del bloque */
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
}
