package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Representa un rol del sistema (por ejemplo: ADMIN, THERAPIST, PATIENT).
 */
@Entity
@Table(name = "ps_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    /** ID único del rol */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;

    /** Nombre del rol (único) */
    @Column(nullable = false, unique = true)
    private String role;

    /** Descripción del rol */
    @Column(nullable = false)
    private String description;
}
