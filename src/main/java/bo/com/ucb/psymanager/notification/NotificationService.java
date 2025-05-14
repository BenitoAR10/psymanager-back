package bo.com.ucb.psymanager.notification;

import bo.com.ucb.psymanager.events.AppointmentBookedEvent;
import bo.com.ucb.psymanager.events.AppointmentRejectedEvent;
import bo.com.ucb.psymanager.events.AppointmentReminderEvent;

/**
 * Interfaz que define el contrato para los servicios de notificación del sistema.
 * Permite orquestar el envío por diferentes canales (WhatsApp, SMS, Email, etc.)
 * en función del tipo de evento recibido (confirmación, rechazo o recordatorio).
 */
public interface NotificationService {

    /**
     * Envía notificaciones por todos los canales configurados
     * cuando una cita ha sido confirmada por el terapeuta.
     *
     * @param event evento de confirmación de cita
     */
    void sendAllChannels(AppointmentBookedEvent event);

    /**
     * Envía notificaciones por todos los canales configurados
     * cuando una cita ha sido rechazada.
     *
     * @param event evento de rechazo de cita
     */
    void sendAllChannels(AppointmentRejectedEvent event);

    /**
     * Envía notificaciones por todos los canales configurados
     * como recordatorio previo a la cita.
     *
     * @param event evento de recordatorio programado
     */
    void sendAllChannels(AppointmentReminderEvent event);
}
