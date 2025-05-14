package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Representa a un terapeuta registrado, vinculado a un usuario base.
 * Usa clave primaria compartida con ps_user.
 */
@Entity
@Table(name = "ps_user_therapist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTherapist {

    /** ID del terapeuta (compartido con el ID del usuario) */
    @Id
    @Column(name = "user_therapist_id")
    private Long userTherapistId;

    /** Usuario base vinculado a este terapeuta */
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_therapist_id")
    private User user;
}
