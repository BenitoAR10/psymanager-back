package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.*;
import bo.com.ucb.psymanager.dto.CreateTreatmentPlanRequestDto;
import bo.com.ucb.psymanager.dto.TreatmentPlanDto;
import bo.com.ucb.psymanager.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TreatmentBlTest {

    @Mock
    private TreatmentDao treatmentDao;

    @Mock
    private CloseTreatmentDao closeTreatmentDao;

    @Mock
    private ScheduledSessionDao scheduleSessionDao;

    @Mock
    private UserPatientDao userPatientDao;

    @Mock
    private UserTherapistDao userTherapistDao;

    @Mock
    private CaseFileDao caseFileDao;

    @InjectMocks
    private TreatmentBl treatmentBl;

    private UserPatient patient;
    private UserTherapist therapist;
    private CreateTreatmentPlanRequestDto request;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUserId(1L);
        user.setFirstName("Ana");
        user.setLastName("Martinez");

        patient = new UserPatient();
        patient.setUserPatientId(1L);
        patient.setUser(user);

        therapist = new UserTherapist();
        therapist.setUserTherapistId(2L);
        therapist.setUser(user);

        request = new CreateTreatmentPlanRequestDto(
                1L,
                2L,
                LocalDateTime.of(2025, 5, 15, 10, 0),
                true,
                5,
                1,
                "Ansiedad generalizada",
                "2025-1"
        );
    }

    @Test
    void createTreatmentPlan_Recurrent_Success() {
        when(scheduleSessionDao.existsByUserPatient_UserPatientIdAndStateIn(anyInt(), any()))
                .thenReturn(true);

        when(treatmentDao.existsByUserPatient_UserPatientIdAndEndDateGreaterThanEqual(anyLong(), any()))
                .thenReturn(false);

        when(userPatientDao.findById(1L)).thenReturn(Optional.of(patient));
        when(userTherapistDao.findById(2L)).thenReturn(Optional.of(therapist));

        Treatment savedTreatment = new Treatment();
        savedTreatment.setTreatmentId(99L);
        savedTreatment.setUserPatient(patient);
        savedTreatment.setUserTherapist(therapist);
        savedTreatment.setStartDate(request.getFirstSessionDateTime().toLocalDate());
        savedTreatment.setEndDate(request.getFirstSessionDateTime().toLocalDate().plusWeeks(4));

        when(treatmentDao.save(any())).thenReturn(savedTreatment);

        TreatmentPlanDto result = treatmentBl.createTreatmentPlan(request);

        assertNotNull(result);
        assertEquals(99L, result.getId());
        assertEquals(patient.getUserPatientId(), result.getPatientId());
        assertEquals(therapist.getUserTherapistId(), result.getTherapistId());
        assertEquals(request.getFirstSessionDateTime(), result.getFirstSessionDateTime());
        assertTrue(result.isRecurrent());
        assertEquals(5, result.getNumberOfSessions());
        assertEquals(1, result.getIntervalWeeks());
        assertEquals(request.getFirstSessionDateTime().toLocalDate(), result.getStartDate());
    }

    @Test
    void createTreatmentPlan_NonRecurrent_Success() {
        request.setRecurrent(false);

        when(scheduleSessionDao.existsByUserPatient_UserPatientIdAndStateIn(anyInt(), any()))
                .thenReturn(true);

        when(treatmentDao.existsByUserPatient_UserPatientIdAndEndDateGreaterThanEqual(anyLong(), any()))
                .thenReturn(false);

        when(userPatientDao.findById(1L)).thenReturn(Optional.of(patient));
        when(userTherapistDao.findById(2L)).thenReturn(Optional.of(therapist));

        Treatment savedTreatment = new Treatment();
        savedTreatment.setTreatmentId(100L);
        savedTreatment.setUserPatient(patient);
        savedTreatment.setUserTherapist(therapist);
        savedTreatment.setStartDate(request.getFirstSessionDateTime().toLocalDate());
        savedTreatment.setEndDate(request.getFirstSessionDateTime().toLocalDate());
        savedTreatment.setSessions(List.of());
        savedTreatment.setEndDate(savedTreatment.getStartDate());

        when(treatmentDao.save(any())).thenReturn(savedTreatment);

        TreatmentPlanDto result = treatmentBl.createTreatmentPlan(request);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertFalse(result.isRecurrent());
        assertEquals(0, result.getIntervalWeeks());

        System.out.println("Interval weeks = " + result.getIntervalWeeks());
        System.out.println("Number of sessions = " + result.getNumberOfSessions());

    }

}
