package bo.com.ucb.psymanager.exceptions;

/**
 * Se lanza cuando no se encuentra el horario solicitado en el sistema.
 */
@SuppressWarnings("")
public class ScheduleNotFoundException extends RuntimeException {
    public ScheduleNotFoundException(String message) {
        super(message);
    }
}