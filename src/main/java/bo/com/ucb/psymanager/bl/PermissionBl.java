package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.RolePermissionDao;
import bo.com.ucb.psymanager.dao.UserRoleDao;
import bo.com.ucb.psymanager.entities.User;
import bo.com.ucb.psymanager.entities.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Lógica de negocio para verificar si un usuario tiene permisos específicos,
 * considerando únicamente los permisos asignados por sus roles.
 */
@Slf4j
@Service
public class PermissionBl {

    private final UserRoleDao userRoleDao;
    private final RolePermissionDao rolePermissionDao;

    @Autowired
    public PermissionBl(UserRoleDao userRoleDao, RolePermissionDao rolePermissionDao) {
        this.userRoleDao = userRoleDao;
        this.rolePermissionDao = rolePermissionDao;
    }

    /**
     * Verifica si el usuario tiene un permiso específico basado en sus roles.
     *
     * @param user Usuario autenticado
     * @param permissionName Nombre del permiso a verificar
     * @return true si el usuario tiene el permiso, false en caso contrario
     */
    public boolean hasPermission(User user, String permissionName) {
        List<String> permissions = rolePermissionDao.findPermissionNamesByUser(user);
        boolean result = permissions.contains(permissionName);
        log.info("Verificando permiso [{}] para usuario {}: {}", permissionName, user.getUserId(), result ? "PERMITIDO" : "DENEGADO");
        return result;
    }

    /**
     * Devuelve todos los permisos del usuario basados únicamente en sus roles.
     *
     * @param user Usuario autenticado
     * @return Lista de nombres de permisos (sin duplicados)
     */
    public List<String> getPermissionsByUser(User user) {
        List<String> permissions = rolePermissionDao.findPermissionNamesByUser(user);
        log.info("Permisos del usuario {}: {}", user.getUserId(), permissions);
        return permissions;
    }

}

