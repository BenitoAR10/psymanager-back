package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.CanceledSession;
import bo.com.ucb.psymanager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CanceledSessionDao extends JpaRepository<CanceledSession, Long> {

    /**
     * Busca todas las sesiones canceladas por un usuario específico dentro de un rango de fechas.
     *
     * @param user El usuario que canceló las sesiones.
     * @param from Fecha de inicio (inclusive).
     * @param to   Fecha de fin (inclusive).
     * @return Lista de sesiones canceladas por el usuario en ese rango.
     */
    List<CanceledSession> findByCanceledByAndCancellationDateBetween(User user, LocalDateTime from, LocalDateTime to);
}