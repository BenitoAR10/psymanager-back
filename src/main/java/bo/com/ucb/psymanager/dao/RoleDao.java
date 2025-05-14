package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * DAO para acceder a los roles disponibles en el sistema.
 */
@Repository
public interface RoleDao extends JpaRepository<Role, Long> {

    /**
     * Busca un rol por su nombre.
     *
     * @param role nombre del rol (ej: ADMIN, PATIENT)
     * @return rol encontrado, si existe
     */
    Optional<Role> findByRole(String role);
}
