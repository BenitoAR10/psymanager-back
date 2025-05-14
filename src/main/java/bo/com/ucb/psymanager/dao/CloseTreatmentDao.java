package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.CloseTreatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DAO para acceder a cierres de tratamientos registrados.
 */
@Repository
public interface CloseTreatmentDao extends JpaRepository<CloseTreatment, Long> {

    /**
     * Verifica si un tratamiento ha sido cerrado.
     *
     * @param treatmentId ID del tratamiento
     * @return true si existe cierre
     */
    boolean existsByTreatment_TreatmentId(Long treatmentId);

    /**
     * Obtiene todos los cierres realizados por un terapeuta específico.
     *
     * @param therapistId ID del terapeuta
     * @return lista de cierres
     */
    List<CloseTreatment> findByTreatment_UserTherapist_UserTherapistId(Long therapistId);

    /**
     * Busca el cierre de un tratamiento específico.
     *
     * @param treatmentId ID del tratamiento
     * @return cierre si existe
     */
    Optional<CloseTreatment> findByTreatment_TreatmentId(Long treatmentId);
}
