package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.dto.CategoryCountResponseDto;
import bo.com.ucb.psymanager.dto.DailyCountProjection;
import bo.com.ucb.psymanager.dto.HourlyCountProjection;
import bo.com.ucb.psymanager.dto.WeeklyCountProjection;
import bo.com.ucb.psymanager.entities.CompletedExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


/**
 * DAO para acceder a los registros de ejercicios completados por los estudiantes.
 * Permite obtener estadísticas agregadas (total, semanal, por categoría).
 */
@Repository
public interface CompletedExerciseDao extends JpaRepository<CompletedExercise, Long> {

    /**
     * Cuenta el total de ejercicios completados por un paciente en un rango de fechas.
     *
     * @param patientId ID del paciente
     * @param start     Inicio del rango (inclusive)
     * @param end       Fin del rango (inclusive)
     * @return Número total de registros
     */
    @Query("""
      SELECT COUNT(ce)
      FROM CompletedExercise ce
      WHERE ce.userPatient.userPatientId = :patientId
        AND ce.completedAt BETWEEN :start AND :end
    """)
    Long countTotalByPatientAndPeriod(
            @Param("patientId") Long patientId,
            @Param("start") LocalDateTime start,
            @Param("end")   LocalDateTime end
    );

    /**
     * Obtiene el conteo de ejercicios completados agrupado por semana ISO.
     * Utiliza una consulta nativa de Postgres para truncar la fecha a la semana.
     *
     * @param patientId ID del paciente
     * @param start     Inicio del rango (inclusive)
     * @param end       Fin del rango (inclusive)
     * @return Lista de proyecciones con año, semana y conteo
     */
    @Query(value = """
      SELECT
        EXTRACT(YEAR   FROM date_trunc('week', completed_at)) AS year,
        EXTRACT(WEEK   FROM date_trunc('week', completed_at)) AS week,
        COUNT(*)                                          AS count
      FROM ps_completed_exercise
      WHERE user_id    = :patientId
        AND completed_at BETWEEN :start AND :end
      GROUP BY year, week
      ORDER BY year, week
    """, nativeQuery = true)
    List<WeeklyCountProjection> findWeeklyCountsByPatientAndPeriod(
            @Param("patientId") Long patientId,
            @Param("start")     LocalDateTime start,
            @Param("end")       LocalDateTime end
    );

    /**
     * Obtiene el conteo de ejercicios completados agrupado por categoría.
     *
     * @param patientId ID del paciente
     * @param start     Inicio del rango (inclusive)
     * @param end       Fin del rango (inclusive)
     * @return Lista de DTOs con categoría y conteo
     */
    @Query("""
      SELECT new bo.com.ucb.psymanager.dto.CategoryCountResponseDto(
        ce.exercise.category, COUNT(ce)
      )
      FROM CompletedExercise ce
      WHERE ce.userPatient.userPatientId = :patientId
        AND ce.completedAt BETWEEN :start AND :end
      GROUP BY ce.exercise.category
    """)
    List<CategoryCountResponseDto> findCategoryCountsByPatientAndPeriod(
            @Param("patientId") Long patientId,
            @Param("start")     LocalDateTime start,
            @Param("end")       LocalDateTime end
    );
    /**
     * Conteo diario de ejercicios completados para un paciente y rango dado.
     */
    @Query(value = """
      SELECT
        date_trunc('day', completed_at)::date AS day,
        COUNT(*) AS count
      FROM ps_completed_exercise
      WHERE user_id      = :patientId
        AND completed_at BETWEEN :start AND :end
      GROUP BY day
      ORDER BY day
      """, nativeQuery = true)
    List<DailyCountProjection> findDailyCountsByPatientAndPeriod(
            @Param("patientId") Long patientId,
            @Param("start") LocalDateTime start,
            @Param("end")   LocalDateTime end
    );

    /**
     * Conteo horario de ejercicios completados para un paciente y rango dado.
     */
    @Query(value = """
      SELECT
        date_trunc('hour', completed_at)          AS hour,
        COUNT(*)                                  AS count
      FROM ps_completed_exercise
      WHERE user_id      = :patientId
        AND completed_at BETWEEN :start AND :end
      GROUP BY hour
      ORDER BY hour
      """, nativeQuery = true)
    List<HourlyCountProjection> findHourlyCountsByPatientAndPeriod(
            @Param("patientId") Long patientId,
            @Param("start") LocalDateTime start,
            @Param("end")   LocalDateTime end
    );
}