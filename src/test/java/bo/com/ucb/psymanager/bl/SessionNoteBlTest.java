package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.SessionNoteDao;
import bo.com.ucb.psymanager.dao.TreatmentSessionDao;
import bo.com.ucb.psymanager.dto.CreateOrUpdateSessionNoteDto;
import bo.com.ucb.psymanager.dto.SessionNoteDto;
import bo.com.ucb.psymanager.entities.SessionNote;
import bo.com.ucb.psymanager.entities.TreatmentSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionNoteBlTest {

    @Mock private SessionNoteDao sessionNoteDao;
    @Mock private TreatmentSessionDao treatmentSessionDao;

    @InjectMocks private SessionNoteBl sessionNoteBl;

    private TreatmentSession treatmentSession;

    @BeforeEach
    void setUp() {
        treatmentSession = new TreatmentSession();
        treatmentSession.setTreatmentSessionId(1L);
    }

    @Test
    void saveOrUpdateSessionNote_CreatesNewNote_Success() {
        CreateOrUpdateSessionNoteDto dto = new CreateOrUpdateSessionNoteDto(
                1L,
                "Ansiedad en clase",
                "Se discutieron técnicas de respiración",
                "Paciente responde bien al enfoque",
                "Revisión de pensamientos automáticos"
        );

        when(treatmentSessionDao.findById(1L)).thenReturn(Optional.of(treatmentSession));
        when(sessionNoteDao.findByTreatmentSession_TreatmentSessionId(1L)).thenReturn(Optional.empty());
        when(sessionNoteDao.save(any())).thenAnswer(inv -> inv.getArgument(0));

        SessionNoteDto result = sessionNoteBl.saveOrUpdateSessionNote(dto);

        assertNotNull(result);
        assertEquals(1L, result.getTreatmentSessionId());
        assertEquals(dto.getTopicAddressed(), result.getTopicAddressed());
    }

    @Test
    void getNoteBySessionId_ReturnsNote_Success() {
        SessionNote note = new SessionNote();
        note.setSessionNoteId(10L);
        note.setTreatmentSession(treatmentSession);
        note.setTopicAddressed("Técnicas de relajación");
        note.setSessionSummary("Resumen corto");
        note.setRelevantObservations("Sin observaciones relevantes");
        note.setNextTopic("Hablar con profesores");
        note.setCreatedAt(LocalDateTime.now());

        when(sessionNoteDao.findByTreatmentSession_TreatmentSessionId(1L))
                .thenReturn(Optional.of(note));

        Optional<SessionNoteDto> result = sessionNoteBl.getNoteBySessionId(1L);

        assertTrue(result.isPresent());
        assertEquals(10L, result.get().getSessionNoteId());
        assertEquals("Técnicas de relajación", result.get().getTopicAddressed());
    }
}
