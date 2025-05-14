package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.*;
import bo.com.ucb.psymanager.dto.TreatmentSessionDto;
import bo.com.ucb.psymanager.entities.*;
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
public class TreatmentSessionBlTest {

    @Mock private TreatmentDao treatmentDao;
    @Mock private TreatmentSessionDao treatmentSessionDao;
    @Mock private ScheduledSessionDao scheduleSessionDao;
    @Mock private TherapistScheduledDao therapistScheduledDao;
    @Mock private UserTherapistDao userTherapistDao;

    @InjectMocks
    private TreatmentSessionBl treatmentSessionBl;

    private Treatment treatment;
    private TherapistScheduled ts;
    private UserTherapist therapist;
    private UserPatient patient;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUserId(1L);
        user.setFirstName("Test");
        user.setLastName("User");

        therapist = new UserTherapist();
        therapist.setUserTherapistId(1L);
        therapist.setUser(user);

        patient = new UserPatient();
        patient.setUserPatientId(2L);
        patient.setUser(user);

        treatment = new Treatment();
        treatment.setTreatmentId(10L);
        treatment.setUserTherapist(therapist);
        treatment.setUserPatient(patient);

        ts = new TherapistScheduled();
        ts.setTherapistScheduledId(123L);
        ts.setUserTherapistId(1);
        ts.setDate(LocalDate.now().plusDays(1));
        ts.setStartTime(LocalTime.of(10, 0));
        ts.setEndTime(LocalTime.of(11, 0));
    }

    @Test
    void addSessionsToPlan_Success() {
        when(treatmentDao.findById(10L)).thenReturn(Optional.of(treatment));
        when(therapistScheduledDao.findById(123L)).thenReturn(Optional.of(ts));
        when(scheduleSessionDao.existsByTherapistScheduled_TherapistScheduledIdAndStateIn(eq(123L), any()))
                .thenReturn(false);

        ScheduleSession savedSlot = new ScheduleSession();
        savedSlot.setScheduleSessionId(555L);
        savedSlot.setTherapistScheduled(ts);
        savedSlot.setUserPatient(patient);
        savedSlot.setState(SessionState.ACCEPTED);

        when(scheduleSessionDao.save(any())).thenReturn(savedSlot);
        when(treatmentSessionDao.countByTreatment_TreatmentId(10L)).thenReturn(0);
        when(treatmentSessionDao.save(any())).thenAnswer(inv -> inv.getArgument(0));

        List<TreatmentSessionDto> result = treatmentSessionBl.addSessionsToPlan(10L, List.of(123L));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ACCEPTED", result.get(0).getState());
        assertEquals(10L, result.get(0).getTreatmentPlanId());
    }

    @Test
    void markSessionAsCompleted_Success() {
        TherapistScheduled ts = new TherapistScheduled();
        ts.setDate(LocalDate.now().minusDays(1));
        ts.setStartTime(LocalTime.of(10, 0));
        ts.setEndTime(LocalTime.of(11, 0));
        ts.setUserTherapistId(1);

        ScheduleSession ss = new ScheduleSession();
        ss.setTherapistScheduled(ts);

        TreatmentSession session = new TreatmentSession();
        session.setTreatmentSessionId(1L);
        session.setScheduleSession(ss);
        session.setCompleted(false);

        when(treatmentSessionDao.findById(1L)).thenReturn(Optional.of(session));

        treatmentSessionBl.markSessionAsCompleted(1L);

        assertTrue(session.getCompleted());
        verify(treatmentSessionDao).save(session);
    }
}
