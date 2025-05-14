package bo.com.ucb.psymanager.events;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Evento publicado cuando una cita es rechazada por el terapeuta.
 * Permite notificar al paciente y actualizar el sistema.
 *
 * @param appointmentId ID de la cita rechazada
 * @param patientId     ID del paciente afectado
 * @param therapistId   ID del terapeuta que rechazó
 * @param dateTime      Fecha y hora original de la cita
 * @param patientPhone  Teléfono del paciente (formato WhatsApp)
 * @param therapistName Nombre del terapeuta
 */
public record AppointmentRejectedEvent(
        Long appointmentId,
        Long patientId,
        Long therapistId,
        LocalDateTime dateTime,
        String patientPhone,
        String therapistName
) {
    public AppointmentRejectedEvent {
        Objects.requireNonNull(appointmentId, "appointmentId no puede ser nulo");
        Objects.requireNonNull(patientId, "patientId no puede ser nulo");
        Objects.requireNonNull(therapistId, "therapistId no puede ser nulo");
        Objects.requireNonNull(dateTime, "dateTime no puede ser nulo");
        Objects.requireNonNull(patientPhone, "patientPhone no puede ser nulo");
        Objects.requireNonNull(therapistName, "therapistName no puede ser nulo");
    }
}
