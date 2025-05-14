package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.SessionNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * DAO para acceder a notas de sesiones específicas dentro de tratamientos.
 */
@Repository
public interface SessionNoteDao extends JpaRepository<SessionNote, Long> {

    /**
     * Obtiene la nota asociada a una sesión de tratamiento.
     *
     * @param treatmentSessionId ID de la sesión
     * @return nota correspondiente, si existe
     */
    Optional<SessionNote> findByTreatmentSession_TreatmentSessionId(Long treatmentSessionId);
}
