package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.UserPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO para acceder a la información de los pacientes registrados en el sistema.
 * Usa clave primaria compartida con la entidad User.
 */
@Repository
public interface UserPatientDao extends JpaRepository<UserPatient, Long> {
    // Por ahora solo operaciones básicas (findById, save, delete, etc.)
}
