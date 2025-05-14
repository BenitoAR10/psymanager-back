package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Representa el cierre de un tratamiento, incluyendo motivo y fecha de cierre.
 * Se relaciona uno a uno con un Treatment mediante clave compartida.
 */
@Entity
@Table(name = "ps_close_treatment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CloseTreatment {

    /** ID del tratamiento cerrado (FK y PK compartida con Treatment) */
    @Id
    @Column(name = "closed_treatment_id")
    private Long closedTreatmentId;

    /** Tratamiento asociado a este cierre */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "closed_treatment_id")
    private Treatment treatment;

    /** Fecha en la que se cierra el tratamiento */
    @Column(name = "closing_date", nullable = false)
    private LocalDate closingDate;

    /** Motivo del cierre del tratamiento */
    @Column(name = "reason_for_closure", columnDefinition = "text")
    private String reasonForClosure;
}
