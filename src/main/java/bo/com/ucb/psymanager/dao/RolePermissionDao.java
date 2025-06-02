package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.Role;
import bo.com.ucb.psymanager.entities.RolePermission;
import bo.com.ucb.psymanager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Acceso a datos para obtener permisos asociados a roles del sistema.
 */
public interface RolePermissionDao extends JpaRepository<RolePermission, Long> {

    /**
     * Retorna la lista de permisos asignados a un rol específico.
     *
     * @param role el rol del cual se quieren obtener los permisos
     * @return lista de asociaciones RolePermission para el rol
     */
    List<RolePermission> findByRole(Role role);

    /**
     * Obtiene los nombres de los permisos asociados a un usuario a través de sus roles.
     *
     * @param user el usuario del cual se quieren obtener los permisos
     * @return lista de nombres de permisos asignados al usuario
     */
    @Query("SELECT rp.permission.permissionName " +
            "FROM UserRole ur " +
            "JOIN ur.role r " +
            "JOIN RolePermission rp ON rp.role = r " +
            "WHERE ur.user = :user")
    List<String> findPermissionNamesByUser(@Param("user") User user);

}