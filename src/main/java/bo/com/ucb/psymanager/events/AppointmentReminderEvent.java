package bo.com.ucb.psymanager.events;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Evento publicado para enviar un recordatorio previo a la cita programada.
 * Generalmente se emite 24h antes mediante una cola con TTL.
 *
 * @param appointmentId   ID de la cita
 * @param patientId       ID del paciente
 * @param appointmentTime Fecha y hora original de la cita
 * @param reminderTime    Momento en que se debe enviar el recordatorio
 * @param patientPhone    Teléfono del paciente (formato WhatsApp)
 * @param therapistName   Nombre del terapeuta
 */
public record AppointmentReminderEvent(
        Long appointmentId,
        Long patientId,
        LocalDateTime appointmentTime,
        LocalDateTime reminderTime,
        String patientPhone,
        String therapistName
) {
    public AppointmentReminderEvent {
        Objects.requireNonNull(appointmentId, "appointmentId no puede ser nulo");
        Objects.requireNonNull(patientId, "patientId no puede ser nulo");
        Objects.requireNonNull(appointmentTime, "appointmentTime no puede ser nulo");
        Objects.requireNonNull(reminderTime, "reminderTime no puede ser nulo");
        Objects.requireNonNull(patientPhone, "patientPhone no puede ser nulo");
        Objects.requireNonNull(therapistName, "therapistName no puede ser nulo");
    }
}
