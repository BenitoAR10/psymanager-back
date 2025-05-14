package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.TreatmentSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO para acceder a las sesiones vinculadas a planes de tratamiento.
 */
@Repository
public interface TreatmentSessionDao extends JpaRepository<TreatmentSession, Long> {

    /**
     * Cuenta cuántas sesiones tiene un tratamiento.
     *
     * @param treatmentId ID del tratamiento
     * @return cantidad de sesiones registradas
     */
    int countByTreatment_TreatmentId(Long treatmentId);

    /**
     * Obtiene todas las sesiones asociadas a un tratamiento.
     *
     * @param treatmentId ID del tratamiento
     * @return lista de sesiones
     */
    List<TreatmentSession> findByTreatment_TreatmentId(Long treatmentId);

    /**
     * Obtiene todas las sesiones ordenadas por su número dentro del tratamiento.
     *
     * @param treatmentId ID del tratamiento
     * @return lista ordenada de sesiones
     */
    List<TreatmentSession> findByTreatment_TreatmentIdOrderBySessionOrderAsc(Long treatmentId);
}
