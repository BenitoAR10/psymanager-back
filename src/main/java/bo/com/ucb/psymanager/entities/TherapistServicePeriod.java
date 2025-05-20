package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "ps_therapist_service_period")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TherapistServicePeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "therapist_service_period_id")
    private Integer therapistServicePeriodId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "position", length = 100)
    private String position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_therapist_id", nullable = false)
    private UserTherapist userTherapist;
}
