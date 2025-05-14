package bo.com.ucb.psymanager.exceptions;

/**
 * Se lanza cuando un usuario intenta acceder a funcionalidades reservadas para pacientes.
 */
@SuppressWarnings("")
public class UserNotPatientException extends RuntimeException {
  public UserNotPatientException(String message) {
    super(message);
  }
}