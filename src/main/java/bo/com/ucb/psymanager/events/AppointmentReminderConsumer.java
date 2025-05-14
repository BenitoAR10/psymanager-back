package bo.com.ucb.psymanager.events;

import bo.com.ucb.psymanager.config.RabbitConfig;
import bo.com.ucb.psymanager.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumidor de eventos de recordatorio de cita.
 * Escucha desde RabbitMQ y activa el servicio de notificación.
 */
@Component
@RequiredArgsConstructor
public class AppointmentReminderConsumer {

    private final NotificationService notificationService;

    /**
     * Se ejecuta al recibir un evento de recordatorio de cita.
     *
     * @param event evento de recordatorio (enviado con TTL desde la cola delay)
     */
    @RabbitListener(queues = RabbitConfig.REMINDER_QUEUE_NAME)
    public void onAppointmentReminder(AppointmentReminderEvent event) {
        notificationService.sendAllChannels(event);
    }
}
