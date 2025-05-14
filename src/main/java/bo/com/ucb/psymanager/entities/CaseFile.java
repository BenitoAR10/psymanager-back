package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Representa la ficha clínica asociada a un plan de tratamiento.
 * Incluye resumen general y recomendaciones del terapeuta.
 */
@Entity
@Table(name = "ps_case_file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaseFile {

    /** Identificador único de la ficha clínica */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long caseFileId;

    /** Tratamiento asociado a esta ficha clínica */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id", nullable = false)
    private Treatment treatment;

    /** Fecha en que se registró la ficha clínica */
    @Column(nullable = false)
    private LocalDate date;

    /** Resumen general del caso */
    @Column(columnDefinition = "text")
    private String summary;

    /** Recomendaciones dadas al paciente */
    @Column(columnDefinition = "text")
    private String recommendations;
}
