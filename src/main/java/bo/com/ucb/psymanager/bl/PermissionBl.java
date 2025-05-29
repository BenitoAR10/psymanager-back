package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.RolePermissionDao;
import bo.com.ucb.psymanager.dao.UserPermissionDao;
import bo.com.ucb.psymanager.dao.UserRoleDao;
import bo.com.ucb.psymanager.entities.User;
import bo.com.ucb.psymanager.entities.UserPermission;
import bo.com.ucb.psymanager.entities.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Lógica de negocio para verificar si un usuario tiene permisos específicos,
 * considerando tanto los asignados por roles como los permisos directos.
 */
@Service
public class PermissionBl {

    private final UserRoleDao userRoleDao;
    private final RolePermissionDao rolePermissionDao;
    private final UserPermissionDao userPermissionDao;

    @Autowired
    public PermissionBl(UserRoleDao userRoleDao, RolePermissionDao rolePermissionDao, UserPermissionDao userPermissionDao) {
        this.userRoleDao = userRoleDao;
        this.rolePermissionDao = rolePermissionDao;
        this.userPermissionDao = userPermissionDao;
    }

    /**
     * Verifica si el usuario tiene un permiso específico, ya sea por su(s) rol(es)
     * o por asignación directa.
     *
     * @param user Usuario autenticado
     * @param permissionName Nombre del permiso a verificar
     * @return true si el usuario tiene el permiso, false en caso contrario
     */
    public boolean hasPermission(User user, String permissionName) {
        Set<String> effectivePermissions = new HashSet<>();

        // 1. Permisos por rol
        List<UserRole> userRoles = userRoleDao.findByUser(user);
        userRoles.forEach(userRole -> {
            rolePermissionDao.findByRole(userRole.getRole()).forEach(rp ->
                    effectivePermissions.add(rp.getPermission().getPermissionName())
            );
        });

        // 2. Permisos asignados directamente al usuario
        List<UserPermission> userPermissions = userPermissionDao.findByUser(user);
        userPermissions.forEach(up ->
                effectivePermissions.add(up.getPermission().getPermissionName())
        );

        return effectivePermissions.contains(permissionName);
    }

    /**
     * Devuelve todos los permisos efectivos del usuario, considerando
     * los permisos asignados por roles y directamente al usuario.
     *
     * @param user Usuario autenticado
     * @return Lista de nombres de permisos (sin duplicados)
     */
    public List<String> getPermissionsByUser(User user) {
        Set<String> effectivePermissions = new HashSet<>();

        // Permisos por rol
        List<UserRole> userRoles = userRoleDao.findByUser(user);
        userRoles.forEach(userRole -> {
            rolePermissionDao.findByRole(userRole.getRole()).forEach(rp ->
                    effectivePermissions.add(rp.getPermission().getPermissionName())
            );
        });

        // Permisos directos
        List<UserPermission> userPermissions = userPermissionDao.findByUser(user);
        userPermissions.forEach(up ->
                effectivePermissions.add(up.getPermission().getPermissionName())
        );

        return new ArrayList<>(effectivePermissions);
    }

}
