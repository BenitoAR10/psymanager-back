package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ps_canceled_session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CanceledSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "canceled_session_id")
    private Long canceledSessionId;

    @Column(name = "cancellation_date", nullable = false)
    private LocalDateTime cancellationDate;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT", nullable = false)
    private String cancellationReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canceled_by_user_id", nullable = false)
    private User canceledBy;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_session_id", nullable = false)
    private ScheduleSession scheduleSession;
}