package bo.com.ucb.psymanager.exceptions;

/**
 * Se lanza cuando un horario proporcionado no es válido.
 */
@SuppressWarnings("")
public class InvalidScheduleException extends RuntimeException {
    public InvalidScheduleException(String message) {
        super(message);
    }
}