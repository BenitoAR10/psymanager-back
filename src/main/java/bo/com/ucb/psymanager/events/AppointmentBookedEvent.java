package bo.com.ucb.psymanager.events;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Evento publicado cuando una cita es confirmada por el terapeuta.
 * Este evento puede ser usado para notificar al paciente por diferentes canales.
 *
 * @param appointmentId  ID de la cita confirmada
 * @param patientId      ID del paciente que reservó la cita
 * @param therapistId    ID del terapeuta que confirmó la cita
 * @param dateTime       Fecha y hora programada de la cita
 * @param patientPhone   Teléfono del paciente en formato WhatsApp (ej: whatsapp:+591...)
 * @param therapistName  Nombre completo del terapeuta
 */
public record AppointmentBookedEvent(
        Long appointmentId,
        Long patientId,
        Long therapistId,
        LocalDateTime dateTime,
        String patientPhone,
        String therapistName
) {
    public AppointmentBookedEvent {
        Objects.requireNonNull(appointmentId, "appointmentId no puede ser nulo");
        Objects.requireNonNull(patientId, "patientId no puede ser nulo");
        Objects.requireNonNull(therapistId, "therapistId no puede ser nulo");
        Objects.requireNonNull(dateTime, "dateTime no puede ser nulo");
        Objects.requireNonNull(patientPhone, "patientPhone no puede ser nulo");
        Objects.requireNonNull(therapistName, "therapistName no puede ser nulo");
    }
}
