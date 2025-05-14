package bo.com.ucb.psymanager.exceptions;

/**
 * Se lanza cuando ya existe una sesión en el horario solicitado.
 */
@SuppressWarnings("")
public class SessionAlreadyExistsException extends RuntimeException {
  public SessionAlreadyExistsException(String message) {
    super(message);
  }
}