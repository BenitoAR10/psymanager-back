package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ps_user_wellness_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWellnessStats {

    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private UserPatient userPatient;

    @Column(name = "total_points", nullable = false)
    private Integer totalPoints;
}
