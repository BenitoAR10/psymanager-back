package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.CaseFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * DAO para acceder a fichas clínicas asociadas a tratamientos.
 */
@Repository
public interface CaseFileDao extends JpaRepository<CaseFile, Long> {

    /**
     * Busca la ficha clínica asociada a un tratamiento específico.
     *
     * @param treatmentId ID del tratamiento
     * @return ficha clínica si existe
     */
    Optional<CaseFile> findByTreatment_TreatmentId(Long treatmentId);

    /**
     * Verifica si existe una ficha clínica registrada para un tratamiento.
     *
     * @param treatmentId ID del tratamiento
     * @return true si existe ficha
     */
    boolean existsByTreatment_TreatmentId(Long treatmentId);
}
