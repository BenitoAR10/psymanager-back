package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.User;
import bo.com.ucb.psymanager.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO para acceder a las asignaciones de roles por usuario.
 */
@Repository
public interface UserRoleDao extends JpaRepository<UserRole, Long> {

    /**
     * Obtiene todos los roles asignados a un usuario específico.
     *
     * @param user entidad del usuario
     * @return lista de roles asignados
     */
    List<UserRole> findByUser(User user);
}
