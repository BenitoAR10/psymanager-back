package bo.com.ucb.psymanager.exceptions;

/**
 * Excepción lanzada cuando un número de CI ya existe en el sistema.
 */
public class CiNumberAlreadyExistsException extends RuntimeException {
    public CiNumberAlreadyExistsException(String message) {
        super(message);
    }
}