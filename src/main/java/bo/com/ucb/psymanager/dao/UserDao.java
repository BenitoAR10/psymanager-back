package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * DAO para acceder a los datos básicos de usuarios registrados.
 */
@Repository
public interface UserDao extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su email único.
     *
     * @param email email del usuario
     * @return usuario correspondiente, si existe
     */
    Optional<User> findByEmail(String email);
}
