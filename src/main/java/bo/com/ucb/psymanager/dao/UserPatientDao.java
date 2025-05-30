package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.User;
import bo.com.ucb.psymanager.entities.UserPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * DAO para acceder a la información de los pacientes registrados en el sistema.
 * Usa clave primaria compartida con la entidad User.
 */
@Repository
public interface UserPatientDao extends JpaRepository<UserPatient, Long> {
    /**
     * Busca un UserPatient por el email del usuario base.
     *
     * @param email email institucional del usuario
     * @return UserPatient correspondiente (si existe)
     */
    Optional<UserPatient> findByUser_Email(String email);

    /**
     * Busca un UserPatient por el objeto User asociado.
     *
     */
    Optional<UserPatient> findByUser(User user);

}
