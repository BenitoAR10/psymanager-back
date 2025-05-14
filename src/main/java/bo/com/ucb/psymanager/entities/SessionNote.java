package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Representa una nota registrada por el terapeuta al finalizar una sesión de tratamiento.
 * Incluye resumen, observaciones y el tema propuesto para la siguiente sesión.
 */
@Entity
@Table(name = "ps_session_notes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionNote {

    /** Identificador único de la nota de sesión */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_note_id")
    private Long sessionNoteId;

    /** Sesión de tratamiento a la que pertenece esta nota */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_session_id", nullable = false)
    private TreatmentSession treatmentSession;

    /** Tema principal abordado durante la sesión */
    @Column(name = "topic_addressed", columnDefinition = "text")
    private String topicAddressed;

    /** Resumen general de la sesión */
    @Column(name = "session_summary", columnDefinition = "text")
    private String sessionSummary;

    /** Observaciones relevantes sobre la conducta o evolución del paciente */
    @Column(name = "relevant_observations", columnDefinition = "text")
    private String relevantObservations;

    /** Tema a tratar en la siguiente sesión */
    @Column(name = "next_topic", columnDefinition = "text")
    private String nextTopic;

    /** Fecha y hora en que fue creada la nota */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
