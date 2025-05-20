package bo.com.ucb.psymanager.dao;


import bo.com.ucb.psymanager.entities.TherapistServicePeriod;
import bo.com.ucb.psymanager.entities.UserTherapist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para acceder a los periodos de servicio de un terapeuta.
 * Permite registrar nuevos periodos y consultar el historial profesional.
 */
@Repository
public interface TherapistServicePeriodDao extends JpaRepository<TherapistServicePeriod, Integer> {

    /**
     * Obtiene todos los periodos de servicio registrados para un terapeuta.
     *
     * @param userTherapist terapeuta autenticado
     * @return lista de periodos de servicio
     */
    List<TherapistServicePeriod> findByUserTherapist(UserTherapist userTherapist);
}