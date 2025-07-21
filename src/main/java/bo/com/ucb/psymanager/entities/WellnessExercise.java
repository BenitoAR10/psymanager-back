package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "ps_wellness_exercise")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WellnessExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category; // Ansiedad, Estrés, etc.

    @Column(name = "audio_url", nullable = false)
    private String audioUrl;

    @Column(name = "points_reward", nullable = false)
    private Integer pointsReward;

    @Column(name = "show_points", nullable = false)
    private Boolean showPoints;
}
