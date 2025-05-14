package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.UserTherapist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO para acceder a los terapeutas registrados en el sistema.
 * Relación uno a uno con la entidad User.
 */
@Repository
public interface UserTherapistDao extends JpaRepository<UserTherapist, Long> {
    // Operaciones CRUD básicas por ahora
}
