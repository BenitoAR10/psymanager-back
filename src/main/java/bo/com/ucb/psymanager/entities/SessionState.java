package bo.com.ucb.psymanager.entities;

/**
 * Estado de una sesión programada.
 */
public enum SessionState {
    PENDING,    // Reservada por el paciente, aún no confirmada
    ACCEPTED,   // Confirmada por el terapeuta
    REJECTED,   // Rechazada por el terapeuta
    CANCELED,   // Cancelada por el paciente
    COMPLETED   // Marcada como realizada por el terapeuta
}
