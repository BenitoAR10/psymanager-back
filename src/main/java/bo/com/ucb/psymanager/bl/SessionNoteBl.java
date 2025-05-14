package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.SessionNoteDao;
import bo.com.ucb.psymanager.dao.TreatmentSessionDao;
import bo.com.ucb.psymanager.dto.CreateOrUpdateSessionNoteDto;
import bo.com.ucb.psymanager.dto.SessionNoteDto;
import bo.com.ucb.psymanager.entities.SessionNote;
import bo.com.ucb.psymanager.entities.TreatmentSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Lógica de negocio para el manejo de notas clínicas vinculadas a sesiones de tratamiento.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SessionNoteBl {

    private final SessionNoteDao sessionNoteDao;
    private final TreatmentSessionDao treatmentSessionDao;

    /**
     * Crea o actualiza una nota clínica asociada a una sesión específica.
     *
     * @param dto DTO con los datos de la nota
     * @return DTO de la nota registrada
     */
    public SessionNoteDto saveOrUpdateSessionNote(CreateOrUpdateSessionNoteDto dto) {
        log.info("Guardando nota para sesión ID={}", dto.getTreatmentSessionId());

        TreatmentSession session = treatmentSessionDao.findById(dto.getTreatmentSessionId())
                .orElseThrow(() -> new IllegalArgumentException("Sesión no encontrada: " + dto.getTreatmentSessionId()));

        SessionNote note = sessionNoteDao
                .findByTreatmentSession_TreatmentSessionId(dto.getTreatmentSessionId())
                .orElse(new SessionNote());

        note.setTreatmentSession(session);
        note.setTopicAddressed(dto.getTopicAddressed());
        note.setSessionSummary(dto.getSessionSummary());
        note.setRelevantObservations(dto.getRelevantObservations());
        note.setNextTopic(dto.getNextTopic());

        sessionNoteDao.save(note);
        log.info("Nota guardada correctamente. ID={}", note.getSessionNoteId());

        return mapToDto(note);
    }

    /**
     * Obtiene la nota clínica registrada para una sesión específica.
     *
     * @param treatmentSessionId ID de la sesión
     * @return nota como DTO, si existe
     */
    public Optional<SessionNoteDto> getNoteBySessionId(Long treatmentSessionId) {
        log.info("Consultando nota para sesión ID={}", treatmentSessionId);
        return sessionNoteDao.findByTreatmentSession_TreatmentSessionId(treatmentSessionId)
                .map(this::mapToDto);
    }

    /**
     * Mapea una entidad SessionNote a su correspondiente DTO.
     */
    private SessionNoteDto mapToDto(SessionNote note) {
        return new SessionNoteDto(
                note.getSessionNoteId(),
                note.getTreatmentSession().getTreatmentSessionId(),
                note.getTopicAddressed(),
                note.getSessionSummary(),
                note.getRelevantObservations(),
                note.getNextTopic(),
                note.getCreatedAt()
        );
    }
}
