package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.Treatment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * DAO para acceder a planes de tratamiento almacenados en la base de datos.
 */
@Repository
public interface TreatmentDao extends JpaRepository<Treatment, Long> {

    /**
     * Retorna todos los tratamientos registrados para un paciente específico.
     *
     * @param patientId ID del paciente.
     * @return lista de tratamientos asignados al paciente.
     */
    List<Treatment> findByUserPatient_UserPatientId(Long patientId);

    /**
     * Retorna todos los tratamientos creados por un terapeuta específico.
     *
     * @param therapistId ID del terapeuta.
     * @return lista de tratamientos creados por el terapeuta.
     */
    List<Treatment> findByUserTherapist_UserTherapistId(Long therapistId);

    /**
     * Retorna los tratamientos activos (fecha de finalización igual o posterior a la actual)
     * de un terapeuta, incluyendo sus sesiones asociadas.
     *
     * @param therapistId ID del terapeuta.
     * @param date fecha de corte (usualmente hoy).
     * @return lista de tratamientos activos.
     */
    @EntityGraph(attributePaths = "sessions")
    List<Treatment> findByUserTherapist_UserTherapistIdAndEndDateGreaterThanEqual(Long therapistId, LocalDate date);

    /**
     * Busca el tratamiento activo de un paciente, definido como aquel que no ha sido cerrado.
     *
     * @param patientId ID del paciente.
     * @return tratamiento activo si existe.
     */
    @Query("""
        SELECT t FROM Treatment t
        WHERE t.userPatient.userPatientId = :patientId
        AND NOT EXISTS (
            SELECT 1 FROM CloseTreatment ct
            WHERE ct.treatment = t
        )
    """)
    Optional<Treatment> findActiveTreatmentByPatientId(@Param("patientId") Long patientId);

    /**
     * Verifica si existe un tratamiento vigente para un paciente.
     *
     * @param patientId ID del paciente.
     * @param date fecha actual o futura.
     * @return true si existe tratamiento vigente.
     */
    boolean existsByUserPatient_UserPatientIdAndEndDateGreaterThanEqual(Long patientId, LocalDate date);


    Optional<Treatment> findByPreviousTreatment_TreatmentId(Long previousTreatmentId);

}
