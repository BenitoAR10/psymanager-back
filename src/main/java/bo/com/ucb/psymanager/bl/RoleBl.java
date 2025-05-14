package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.RoleDao;
import bo.com.ucb.psymanager.dao.UserDao;
import bo.com.ucb.psymanager.dao.UserRoleDao;
import bo.com.ucb.psymanager.dao.UserTherapistDao;
import bo.com.ucb.psymanager.entities.Role;
import bo.com.ucb.psymanager.entities.User;
import bo.com.ucb.psymanager.entities.UserRole;
import bo.com.ucb.psymanager.entities.UserTherapist;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleBl {

    private static final Logger logger = Logger.getLogger(RoleBl.class);

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final UserRoleDao userRoleDao;
    private final UserTherapistDao userTherapistDao;

    @Autowired
    public RoleBl(UserDao userDao, RoleDao roleDao, UserRoleDao userRoleDao, UserTherapistDao userTherapistDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.userRoleDao = userRoleDao;
        this.userTherapistDao = userTherapistDao;
    }

    /**
     * Asigna un rol a un usuario si no lo tiene ya asignado.
     *
     * @param email    Email del usuario.
     * @param roleName Nombre del rol a asignar.
     */
    public void assignRoleToUser(String email, String roleName) {
        logger.info("Intentando asignar rol '" + roleName + "' al usuario: " + email);

        User user = userDao.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Usuario no encontrado con email: " + email);
                    return new RuntimeException("User not found with email: " + email);
                });

        Role role = roleDao.findByRole(roleName)
                .orElseThrow(() -> {
                    logger.error("Rol no encontrado: " + roleName);
                    return new RuntimeException("Role not found: " + roleName);
                });

        boolean hasRole = userRoleDao.findByUser(user).stream()
                .anyMatch(userRole -> userRole.getRole().getRole().equals(roleName));

        if (hasRole) {
            logger.warn("El usuario " + email + " ya tiene el rol '" + roleName + "'");
            throw new RuntimeException("User already has role: " + roleName);
        }

        userRoleDao.save(new UserRole(user, role));
        logger.info("Rol '" + roleName + "' asignado exitosamente al usuario " + email);

        if ("THERAPIST".equalsIgnoreCase(roleName)) {
            if (!userTherapistDao.existsById(user.getUserId())) {
                UserTherapist userTherapist = new UserTherapist();
                userTherapist.setUser(user);
                userTherapistDao.save(userTherapist);
                logger.info("Usuario " + email + " añadido a la tabla UserTherapist");
            } else {
                logger.debug("Usuario " + email + " ya existe en la tabla UserTherapist");
            }
        }
    }
}