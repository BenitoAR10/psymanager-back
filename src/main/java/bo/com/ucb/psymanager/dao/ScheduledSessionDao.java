package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.ScheduleSession;
import bo.com.ucb.psymanager.entities.SessionState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DAO para acceder a las sesiones programadas (ScheduleSession),
 * derivadas de los bloques de disponibilidad de los terapeutas.
 */
@Repository
public interface ScheduledSessionDao extends JpaRepository<ScheduleSession, Long> {

    /**
     * Verifica si un bloque de horario del terapeuta ya está ocupado
     * por una sesión en estado ACCEPTED o PENDING.
     *
     * @param therapistScheduledId ID del bloque
     * @param states lista de estados considerados como ocupados
     * @return true si el bloque está ocupado
     */
    boolean existsByTherapistScheduled_TherapistScheduledIdAndStateIn(
            Long therapistScheduledId,
            List<SessionState> states
    );

    /**
     * Obtiene la última sesión programada de un paciente en cierto estado.
     *
     * @param patientId ID del paciente
     * @param state     estado deseado
     * @return sesión más reciente si existe
     */
    Optional<ScheduleSession> findTopByUserPatient_UserPatientIdAndStateOrderByTherapistScheduled_DateDesc(
            Integer patientId,
            SessionState state
    );

    /**
     * Obtiene todas las sesiones asociadas a un bloque de horario específico.
     *
     * @param therapistScheduledId ID del bloque
     * @return lista de sesiones programadas
     */
    List<ScheduleSession> findByTherapistScheduled_TherapistScheduledId(Long therapistScheduledId);

    /**
     * Obtiene todas las sesiones programadas de un paciente.
     *
     * @param userId ID del usuario base del paciente
     * @return lista de sesiones del paciente
     */
    List<ScheduleSession> findByUserPatient_User_UserId(int userId);

    /**
     * Obtiene todas las sesiones de un terapeuta filtradas por estado.
     *
     * @param userTherapistId ID del terapeuta
     * @param states estados deseados
     * @return lista de sesiones filtradas
     */
    List<ScheduleSession> findByTherapistScheduled_UserTherapistIdAndStateIn(
            int userTherapistId,
            List<SessionState> states
    );

    /**
     * Verifica si un paciente tiene al menos una sesión en alguno de los estados dados.
     *
     * @param userPatientId ID del paciente
     * @param states lista de estados
     * @return true si tiene al menos una sesión en esos estados
     */
    boolean existsByUserPatient_UserPatientIdAndStateIn(
            int userPatientId,
            List<SessionState> states
    );

    /**
     * Busca las sesiones programadas por un paciente específico,
     * filtrando por una lista de estados deseados (ej. PENDING, ACCEPTED).
     *
     * @param userPatientId ID del paciente
     * @param states Lista de estados a incluir en la búsqueda
     * @return Lista de sesiones del paciente con estados válidos
     */
    List<ScheduleSession> findByUserPatient_UserPatientIdAndStateIn(
            Integer userPatientId,
            List<SessionState> states
    );
}
