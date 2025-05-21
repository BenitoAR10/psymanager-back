package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ps_completed_exercise")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompletedExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "completed_exercise_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserPatient userPatient;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private WellnessExercise exercise;

    @Column(name = "completed_at", nullable = false)
    private LocalDateTime completedAt;
}
