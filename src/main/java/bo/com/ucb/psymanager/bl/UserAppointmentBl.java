package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.ScheduledSessionDao;
import bo.com.ucb.psymanager.dao.UserDao;
import bo.com.ucb.psymanager.dto.UserAppointmentDetailDto;
import bo.com.ucb.psymanager.dto.UserAppointmentDto;
import bo.com.ucb.psymanager.entities.ScheduleSession;
import bo.com.ucb.psymanager.entities.SessionState;
import bo.com.ucb.psymanager.entities.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAppointmentBl {

    private static final Logger logger = LoggerFactory.getLogger(UserAppointmentBl.class);

    private final ScheduledSessionDao scheduledSessionDao;
    private final UserDao userDao;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Obtiene todas las citas activas agendadas por un paciente específico.
     * Solo se devuelven sesiones en estado PENDING o ACCEPTED.
     *
     * @param userId ID del paciente autenticado.
     * @return Lista de citas activas del paciente.
     */
    public List<UserAppointmentDto> getUserAppointments(Long userId) {
        logger.info("Buscando citas ACTIVAS para el usuario con ID: {}", userId);

        List<ScheduleSession> sessions = scheduledSessionDao
                .findByUserPatient_UserPatientIdAndStateIn(
                        userId.intValue(),
                        List.of(SessionState.ACCEPTED, SessionState.PENDING)
                );

        return sessions.stream().map(session -> {
            String therapistName = userDao.findById((long) session.getTherapistScheduled().getUserTherapistId())
                    .map(user -> user.getFirstName() + " " + user.getLastName())
                    .orElse("Terapeuta no encontrado");

            return new UserAppointmentDto(
                    session.getScheduleSessionId(),
                    therapistName,
                    session.getTherapistScheduled().getDate().format(dateFormatter),
                    session.getTherapistScheduled().getStartTime().format(timeFormatter),
                    session.getTherapistScheduled().getEndTime().format(timeFormatter),
                    session.getState()
            );

        }).collect(Collectors.toList());
    }

    /**
     * Obtiene los detalles de una cita agendada específica por su ID, validando que
     * pertenezca al usuario autenticado.
     *
     * @param sessionId ID de la sesión.
     * @param userId ID del paciente autenticado.
     * @return Detalles de la cita si existe y pertenece al usuario.
     */
    public Optional<UserAppointmentDetailDto> getAppointmentDetail(Long sessionId, Long userId) {
        logger.info("Buscando detalle de cita con ID: {} para usuario con ID: {}", sessionId, userId);

        return scheduledSessionDao.findById(sessionId)
                .filter(session -> session.getUserPatient().getUser().getUserId() == userId.intValue())
                .map(session -> {
                    Optional<User> therapistOpt = userDao.findById((long) session.getTherapistScheduled().getUserTherapistId());

                    String therapistName = therapistOpt
                            .map(user -> user.getFirstName() + " " + user.getLastName())
                            .orElse("Terapeuta no encontrado");

                    String phoneNumber = therapistOpt
                            .map(User::getPhoneNumber)
                            .orElse(null);

                    return new UserAppointmentDetailDto(
                            session.getScheduleSessionId(),
                            therapistName,
                            phoneNumber,
                            session.getTherapistScheduled().getDate().format(dateFormatter),
                            session.getTherapistScheduled().getStartTime().format(timeFormatter),
                            session.getTherapistScheduled().getEndTime().format(timeFormatter),
                            session.getState()
                    );

                });
    }


}
