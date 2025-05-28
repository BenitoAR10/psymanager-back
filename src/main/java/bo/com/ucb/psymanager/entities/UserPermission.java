package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

/**
 * Representa un permiso específico asignado directamente a un usuario,
 * independientemente de los permisos heredados por roles.
 * Esto permite otorgar permisos personalizados a nivel de usuario.
 */
@Entity
@Table(name = "ps_user_permission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPermission {

    /** Identificador único del registro de permiso personalizado */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_permission_id")
    private Long userPermissionId;

    /** Referencia al usuario que recibe este permiso */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Referencia al permiso otorgado directamente */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;
}