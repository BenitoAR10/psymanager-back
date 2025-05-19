package bo.com.ucb.psymanager.exceptions;

/**
 * Se lanza cuando se intenta registrar un usuario con un email
 */
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}