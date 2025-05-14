package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Representa la asignación de un rol específico a un usuario.
 */
@Data
@Entity
@Table(name = "ps_user_role")
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {

    /** ID único de la asignación usuario-rol */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id")
    private Long userRoleId;

    /** Usuario al que se asigna el rol */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Rol asignado al usuario */
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    /** Constructor de conveniencia */
    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
    }
}
