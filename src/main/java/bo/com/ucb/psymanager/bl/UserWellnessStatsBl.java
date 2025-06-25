package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.UserWellnessStatsDao;
import bo.com.ucb.psymanager.entities.UserWellnessStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Lógica de negocio relacionada con las estadísticas de bienestar de los pacientes.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserWellnessStatsBl {

    private final UserWellnessStatsDao statsDao;

    /**
     * Recupera el total de puntos acumulados por un paciente.
     *
     * @param userPatientId el ID del paciente (clave primaria en ps_user_patient y ps_user_wellness_stats)
     * @return el total de puntos si existe un registro, o 0 en caso contrario
     */
    @Transactional(readOnly = true)
    public int getTotalPoints(Long userPatientId) {
        log.info("Obteniendo total de puntos para paciente con ID={}", userPatientId);
        Optional<UserWellnessStats> statsOpt = statsDao.findById(userPatientId);
        int total = statsOpt
                .map(UserWellnessStats::getTotalPoints)
                .orElse(0);
        log.info("Total de puntos obtenido para paciente {}: {}", userPatientId, total);
        return total;
    }
}