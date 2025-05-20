package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ps_therapist_specialty")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TherapistSpecialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "therapist_specialty_id")
    private Integer therapistSpecialtyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_therapist_id", nullable = false)
    private UserTherapist userTherapist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialty_id", nullable = false)
    private Specialty specialty;

}
