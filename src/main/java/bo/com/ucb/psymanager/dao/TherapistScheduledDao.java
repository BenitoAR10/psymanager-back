package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.TherapistScheduled;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * DAO para acceder a los bloques de disponibilidad definidos por terapeutas.
 */
@Repository
public interface TherapistScheduledDao extends JpaRepository<TherapistScheduled, Long> {

    /**
     * Obtiene todos los bloques de disponibilidad registrados por un terapeuta.
     *
     * @param userTherapistId ID del terapeuta
     * @return lista de bloques registrados
     */
    List<TherapistScheduled> findByUserTherapistId(int userTherapistId);

    /**
     * Busca todos los bloques de disponibilidad que ocurren entre dos fechas dadas (inclusive).
     *
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de bloques encontrados en el rango
     */
    List<TherapistScheduled> findByDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Verifica si existe algún bloque que se solape con una nueva franja horaria
     * en una fecha específica.
     * Se considera conflicto si hay intersección horaria.
     *
     * @param date fecha del nuevo bloque
     * @param newStartTime hora de inicio propuesta
     * @param newEndTime hora de fin propuesta
     * @return lista de bloques que entran en conflicto
     */
    @Query("""
        SELECT ts FROM TherapistScheduled ts
        WHERE ts.date = :date
          AND (ts.startTime < :newEndTime AND ts.endTime > :newStartTime)
    """)
    List<TherapistScheduled> findConflictingSchedules(
            @Param("date") LocalDate date,
            @Param("newStartTime") LocalTime newStartTime,
            @Param("newEndTime") LocalTime newEndTime
    );
}
