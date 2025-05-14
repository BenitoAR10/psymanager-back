package bo.com.ucb.psymanager.exceptions;

/**
 * Se lanza cuando existe un conflicto con un horario ya asignado.
 */
@SuppressWarnings("")
public class ScheduleConflictException extends RuntimeException {
    public ScheduleConflictException(String message) {
        super(message);
    }
}