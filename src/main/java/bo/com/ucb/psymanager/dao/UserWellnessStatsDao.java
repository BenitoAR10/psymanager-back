package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.UserPatient;
import bo.com.ucb.psymanager.entities.UserWellnessStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * DAO para acceder a las estadísticas de bienestar de cada paciente,
 * incluyendo el total acumulado de puntos obtenidos por completar ejercicios.
 */
@Repository
public interface UserWellnessStatsDao extends JpaRepository<UserWellnessStats, Long> {

    /**
     * Busca las estadísticas de bienestar para un paciente específico.
     *
     * @param userPatient El paciente cuyo total de puntos se desea consultar.
     * @return Las estadísticas asociadas al paciente, si existen.
     */
    Optional<UserWellnessStats> findByUserPatient(UserPatient userPatient);
}