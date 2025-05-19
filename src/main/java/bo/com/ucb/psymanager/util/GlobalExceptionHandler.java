package bo.com.ucb.psymanager.util;

import bo.com.ucb.psymanager.dto.ErrorResponseDto;
import bo.com.ucb.psymanager.exceptions.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Maneja globalmente todas las excepciones del sistema y
 * devuelve respuestas estandarizadas con código de estado HTTP.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones cuando una entidad no es encontrada en la base de datos.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        log.warn("Entidad no encontrada: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "No se encontró la entidad solicitada", request);
    }

    /**
     * Maneja errores de agenda inválida.
     */
    @ExceptionHandler(InvalidScheduleException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidSchedule(InvalidScheduleException ex, HttpServletRequest request) {
        log.warn("Agenda inválida: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /**
     * Maneja conflictos de horarios.
     */
    @ExceptionHandler(ScheduleConflictException.class)
    public ResponseEntity<ErrorResponseDto> handleScheduleConflict(ScheduleConflictException ex, HttpServletRequest request) {
        log.warn("Conflicto de horario: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    /**
     * Maneja errores cuando ya existe una sesión.
     */
    @ExceptionHandler(SessionAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleSessionExists(SessionAlreadyExistsException ex, HttpServletRequest request) {
        log.warn("Sesión ya existente: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    /**
     * Maneja errores cuando no se encuentra el horario solicitado.
     */
    @ExceptionHandler(ScheduleNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleScheduleNotFound(ScheduleNotFoundException ex, HttpServletRequest request) {
        log.warn("Horario no encontrado: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    /**
     * Maneja errores cuando un usuario no tiene el rol adecuado (no es paciente).
     */
    @ExceptionHandler(UserNotPatientException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotPatient(UserNotPatientException ex, HttpServletRequest request) {
        log.warn("Usuario no es paciente: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /**
     * Maneja errores de argumentos inválidos.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Argumento ilegal: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Solicitud inválida: " + ex.getMessage(), request);
    }

    /**
     * Maneja errores de estado ilegal (acciones que no se pueden realizar).
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalState(IllegalStateException ex, HttpServletRequest request) {
        log.warn("Estado ilegal: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, "No se puede realizar esta acción: " + ex.getMessage(), request);
    }

    /**
     * Método auxiliar para construir respuestas de error uniformes.
     */
    private ResponseEntity<ErrorResponseDto> buildErrorResponse(HttpStatus status, String message, HttpServletRequest request) {
        ErrorResponseDto error = new ErrorResponseDto(status, message, request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    /**
     * Maneja excepciones de correo electrónico ya registrado.
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailExists(EmailAlreadyExistsException ex, HttpServletRequest request) {
        log.warn("Email ya registrado: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    /**
     * Maneja excepciones de número de CI ya registrado.
     */
    @ExceptionHandler(CiNumberAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleCiExists(CiNumberAlreadyExistsException ex, HttpServletRequest request) {
        log.warn("CI ya registrado: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }
}
