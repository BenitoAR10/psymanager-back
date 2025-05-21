package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.CanceledSessionDao;
import bo.com.ucb.psymanager.dao.ScheduledSessionDao;
import bo.com.ucb.psymanager.dao.UserPatientDao;
import bo.com.ucb.psymanager.dto.CancelSessionRequestDto;
import bo.com.ucb.psymanager.entities.*;
import bo.com.ucb.psymanager.entities.SessionState;
import bo.com.ucb.psymanager.exceptions.UserNotPatientException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Lógica dedicada para cancelar sesiones por parte del paciente.
 */
@Service
@RequiredArgsConstructor
public class CancelSessionBl {

    private static final Logger logger = LoggerFactory.getLogger(CancelSessionBl.class);

    private final ScheduledSessionDao scheduledSessionDao;
    private final UserPatientDao userPatientDao;
    private final CanceledSessionDao canceledSessionDao;

    /**
     * Permite que un paciente cancele una sesión previamente aceptada.
     * Aplica validaciones de propiedad, estado, tiempo y límite semanal.
     *
     * @param patientEmail email del paciente autenticado (extraído del token)
     * @param dto          datos de la sesión a cancelar y motivo
     */
    @Transactional
    public void cancelSessionByPatient(String patientEmail, CancelSessionRequestDto dto) {
        logger.info("Iniciando cancelación de sesión por paciente '{}'", patientEmail);

        // Buscar usuario paciente
        UserPatient userPatient = userPatientDao.findByUser_Email(patientEmail)
                .orElseThrow(() -> new UserNotPatientException("No se encontró un paciente con el email: " + patientEmail));

        // Obtener sesión
        ScheduleSession session = scheduledSessionDao.findById(dto.getScheduleSessionId())
                .orElseThrow(() -> new IllegalArgumentException("La sesión no existe."));

        // Verificar propiedad
        if (!session.getUserPatient().getUserPatientId().equals(userPatient.getUserPatientId())) {
            logger.warn("El paciente '{}' intentó cancelar una sesión que no le pertenece (ID={})", patientEmail, dto.getScheduleSessionId());
            throw new IllegalStateException("No tienes permiso para cancelar esta sesión.");
        }

        // Validar estado de la sesión
        if (session.getState() != SessionState.ACCEPTED) {
            logger.warn("El paciente '{}' intentó cancelar una sesión no aceptada (estado actual: {})", patientEmail, session.getState());
            throw new IllegalStateException("Solo se pueden cancelar sesiones en estado ACCEPTED.");
        }

        // Validar tiempo mínimo antes de la sesión (2 horas)
        LocalDateTime sessionTime = session.getTherapistScheduled()
                .getDate()
                .atTime(session.getTherapistScheduled().getStartTime());

        if (sessionTime.minusHours(2).isBefore(LocalDateTime.now())) {
            logger.warn("El paciente '{}' intentó cancelar una sesión a menos de 2h de su inicio", patientEmail);
            throw new IllegalStateException("No puedes cancelar una sesión faltando menos de 2 horas.");
        }

        // Verificar límite de cancelaciones en los últimos 7 días
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        List<CanceledSession> recent = canceledSessionDao
                .findByCanceledByAndCancellationDateBetween(
                        userPatient.getUser(), oneWeekAgo, LocalDateTime.now());

        if (recent.size() >= 2) {
            logger.warn("El paciente '{}' ha superado el límite de cancelaciones esta semana ({} sesiones)", patientEmail, recent.size());
            throw new IllegalStateException("Has alcanzado el límite de cancelaciones permitidas esta semana.");
        }

        // Registrar cancelación
        CanceledSession cancel = new CanceledSession();
        cancel.setCanceledBy(userPatient.getUser());
        cancel.setScheduleSession(session);
        cancel.setCancellationReason(dto.getCancellationReason());
        cancel.setCancellationDate(LocalDateTime.now());
        canceledSessionDao.save(cancel);

        // Actualizar estado a CANCELED
        session.setState(SessionState.CANCELED);
        scheduledSessionDao.save(session);

        logger.info("Sesión ID={} cancelada exitosamente por '{}'. Motivo: '{}'",
                session.getScheduleSessionId(), patientEmail, dto.getCancellationReason());
    }

}
