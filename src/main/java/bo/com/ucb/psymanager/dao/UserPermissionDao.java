package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.User;
import bo.com.ucb.psymanager.entities.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Acceso a datos para los permisos individuales asignados a usuarios.
 */
public interface UserPermissionDao extends JpaRepository<UserPermission, Long> {

    /**
     * Obtiene la lista de permisos asignados directamente a un usuario.
     * @param user el usuario del cual obtener los permisos personalizados
     * @return lista de objetos UserPermission asociados al usuario
     */
    List<UserPermission> findByUser(User user);
}