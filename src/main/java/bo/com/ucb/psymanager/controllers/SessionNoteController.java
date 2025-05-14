package bo.com.ucb.psymanager.controllers;

import bo.com.ucb.psymanager.bl.SessionNoteBl;
import bo.com.ucb.psymanager.dto.CreateOrUpdateSessionNoteDto;
import bo.com.ucb.psymanager.dto.SessionNoteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * API REST para la gestión de notas clínicas asociadas a sesiones de tratamiento.
 */
@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
@Slf4j
public class SessionNoteController {

    private final SessionNoteBl sessionNoteBl;

    /**
     * Registra o actualiza una nota asociada a una sesión de tratamiento.
     *
     * @param dto DTO con los datos de la nota
     * @return nota registrada
     */
    @PostMapping
    public ResponseEntity<SessionNoteDto> saveOrUpdateNote(@RequestBody CreateOrUpdateSessionNoteDto dto) {
        log.info("POST /api/notes → sesión ID={}", dto.getTreatmentSessionId());
        SessionNoteDto result = sessionNoteBl.saveOrUpdateSessionNote(dto);
        return ResponseEntity.ok(result);
    }

    /**
     * Obtiene la nota registrada para una sesión específica.
     *
     * @param sessionId ID de la sesión de tratamiento
     * @return nota encontrada, o 404 si no existe
     */
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<SessionNoteDto> getNoteBySession(@PathVariable Long sessionId) {
        log.info("GET /api/notes/session/{}", sessionId);
        return sessionNoteBl.getNoteBySessionId(sessionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
