package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.*;
import bo.com.ucb.psymanager.dto.UpcomingAppointmentDto;
import bo.com.ucb.psymanager.entities.*;
import bo.com.ucb.psymanager.events.AmqpEventPublisher;
import bo.com.ucb.psymanager.events.AppointmentBookedEvent;
import bo.com.ucb.psymanager.events.AppointmentRejectedEvent;
import bo.com.ucb.psymanager.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScheduledSessionBlTest {

    @Mock
    private ScheduledSessionDao scheduledSessionDao;

    @Mock
    private TherapistScheduledDao therapistScheduledDao;

    @Mock
    private UserPatientDao userPatientDao;

    @Mock
    private UserTherapistDao userTherapistDao;

    @Mock
    private AmqpEventPublisher eventPublisher;

    @InjectMocks
    private ScheduledSessionBl scheduledSessionBl;

    private UserPatient testPatient;
    private TherapistScheduled testSchedule;
    private ScheduleSession testSession;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUserId(1L);
        user.setFirstName("Juan");
        user.setLastName("Perez");
        user.setPhoneNumber("77777777");
        user.setEmail("juan@example.com");

        testPatient = new UserPatient();
        testPatient.setUserPatientId(1L);
        testPatient.setUser(user);

        testSchedule = new TherapistScheduled();
        testSchedule.setTherapistScheduledId(1L);
        testSchedule.setUserTherapistId(1);
        testSchedule.setDate(LocalDate.now().plusDays(1));
        testSchedule.setStartTime(LocalTime.of(10, 0));

        testSession = new ScheduleSession();
        testSession.setScheduleSessionId(1L);
        testSession.setUserPatient(testPatient);
        testSession.setTherapistScheduled(testSchedule);
        testSession.setState(SessionState.PENDING);
    }

    @Test
    void createScheduledSession_Success() {
        when(userPatientDao.findById(1L)).thenReturn(Optional.of(testPatient));
        when(therapistScheduledDao.findById(1L)).thenReturn(Optional.of(testSchedule));
        when(scheduledSessionDao.existsByTherapistScheduled_TherapistScheduledIdAndStateIn(anyLong(), any())).thenReturn(false);
        when(scheduledSessionDao.existsByUserPatient_UserPatientIdAndStateIn(anyInt(), any())).thenReturn(false);
        when(scheduledSessionDao.findTopByUserPatient_UserPatientIdAndStateOrderByTherapistScheduled_DateDesc(anyInt(), eq(SessionState.ACCEPTED)))
                .thenReturn(Optional.empty());
        when(scheduledSessionDao.save(any())).thenReturn(testSession);

        scheduledSessionBl.createScheduledSession(1L, 1L);

        verify(scheduledSessionDao, times(1)).save(any());
    }

    @Test
    void createScheduledSession_UserNotPatient_ThrowsException() {
        when(userPatientDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotPatientException.class, () -> {
            scheduledSessionBl.createScheduledSession(1L, 1L);
        });
    }

    @Test
    void createScheduledSession_ScheduleNotFound_ThrowsException() {
        when(userPatientDao.findById(1L)).thenReturn(Optional.of(testPatient));
        when(therapistScheduledDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ScheduleNotFoundException.class, () -> {
            scheduledSessionBl.createScheduledSession(1L, 1L);
        });
    }

    @Test
    void createScheduledSession_SessionAlreadyExists_ThrowsException() {
        when(userPatientDao.findById(1L)).thenReturn(Optional.of(testPatient));
        when(therapistScheduledDao.findById(1L)).thenReturn(Optional.of(testSchedule));
        when(scheduledSessionDao.existsByTherapistScheduled_TherapistScheduledIdAndStateIn(anyLong(), any())).thenReturn(true);

        assertThrows(SessionAlreadyExistsException.class, () -> {
            scheduledSessionBl.createScheduledSession(1L, 1L);
        });
    }

    @Test
    void updateSessionState_ToAccepted_Success() {
        when(scheduledSessionDao.findById(1L)).thenReturn(Optional.of(testSession));

        UserTherapist therapist = new UserTherapist();
        User therapistUser = new User();
        therapistUser.setFirstName("Carlos");
        therapistUser.setLastName("Lopez");
        therapist.setUser(therapistUser);

        when(userTherapistDao.findById(anyLong())).thenReturn(Optional.of(therapist));

        scheduledSessionBl.updateSessionState(1L, SessionState.ACCEPTED);

        assertEquals(SessionState.ACCEPTED, testSession.getState());
        verify(eventPublisher, times(1)).publishBooked(any(AppointmentBookedEvent.class));
    }

    @Test
    void updateSessionState_ToRejected_Success() {
        when(scheduledSessionDao.findById(1L)).thenReturn(Optional.of(testSession));

        UserTherapist therapist = new UserTherapist();
        User therapistUser = new User();
        therapistUser.setFirstName("Carlos");
        therapistUser.setLastName("Lopez");
        therapist.setUser(therapistUser);

        when(userTherapistDao.findById(anyLong())).thenReturn(Optional.of(therapist));

        scheduledSessionBl.updateSessionState(1L, SessionState.REJECTED);

        assertEquals(SessionState.REJECTED, testSession.getState());
        verify(eventPublisher, times(1)).publishRejected(any(AppointmentRejectedEvent.class));
    }

    @Test
    void updateSessionState_InvalidState_ThrowsException() {

        assertThrows(IllegalArgumentException.class, () -> {
            scheduledSessionBl.updateSessionState(1L, SessionState.AVAILABLE);
        });
    }

    @Test
    void getUpcomingAppointmentsForTherapist_ReturnsFilteredList() {
        ScheduleSession futureSession = new ScheduleSession();
        futureSession.setScheduleSessionId(2L);
        futureSession.setUserPatient(testPatient);
        futureSession.setTherapistScheduled(testSchedule);
        futureSession.setState(SessionState.ACCEPTED);

        ScheduleSession pastSession = new ScheduleSession();
        pastSession.setScheduleSessionId(3L);
        pastSession.setUserPatient(testPatient);

        ScheduleSession acceptedSession = new ScheduleSession();
        acceptedSession.setScheduleSessionId(3L);
        acceptedSession.setUserPatient(testPatient);

        TherapistScheduled pastSchedule = new TherapistScheduled();
        pastSchedule.setTherapistScheduledId(99L);
        pastSchedule.setUserTherapistId(1);
        pastSchedule.setDate(LocalDate.now().minusDays(1));
        pastSchedule.setStartTime(LocalTime.of(10, 0));
        acceptedSession.setTherapistScheduled(pastSchedule);

        pastSession.setTherapistScheduled(pastSchedule);
        pastSession.setState(SessionState.ACCEPTED);

        when(scheduledSessionDao.findByTherapistScheduled_UserTherapistIdAndStateIn(anyInt(), any()))
                .thenReturn(List.of(futureSession, pastSession));

        List<UpcomingAppointmentDto> result = scheduledSessionBl.getUpcomingAppointmentsForTherapist(1L, 10);

        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getAppointmentId());
    }

    @Test
    void getPendingAppointmentsForTherapist_ReturnsOnlyPending() {
        ScheduleSession pendingSession = new ScheduleSession();
        pendingSession.setScheduleSessionId(2L);
        pendingSession.setUserPatient(testPatient);
        pendingSession.setTherapistScheduled(testSchedule);
        pendingSession.setState(SessionState.PENDING);

        ScheduleSession acceptedSession = new ScheduleSession();
        acceptedSession.setScheduleSessionId(3L);
        acceptedSession.setUserPatient(testPatient);
        acceptedSession.setTherapistScheduled(testSchedule);
        acceptedSession.setState(SessionState.ACCEPTED);

        when(scheduledSessionDao.findByTherapistScheduled_UserTherapistIdAndStateIn(anyInt(), eq(List.of(SessionState.PENDING))))
                .thenReturn(List.of(pendingSession));

        List<UpcomingAppointmentDto> result = scheduledSessionBl.getPendingAppointmentsForTherapist(1L);

        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getAppointmentId());
        assertEquals("PENDING", result.get(0).getState());
    }
}
