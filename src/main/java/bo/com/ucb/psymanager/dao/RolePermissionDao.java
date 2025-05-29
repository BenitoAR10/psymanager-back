package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.Role;
import bo.com.ucb.psymanager.entities.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

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
}