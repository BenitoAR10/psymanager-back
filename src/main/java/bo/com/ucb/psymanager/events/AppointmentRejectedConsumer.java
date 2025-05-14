package bo.com.ucb.psymanager.events;

import bo.com.ucb.psymanager.config.RabbitConfig;
import bo.com.ucb.psymanager.notification.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumidor de eventos de citas rechazadas.
 * Escucha mensajes desde RabbitMQ y reenvía las notificaciones correspondientes.
 */
@Component
public class AppointmentRejectedConsumer {

    private final NotificationService notificationService;

    public AppointmentRejectedConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Se ejecuta al recibir un evento de cita rechazada.
     *
     * @param event evento de rechazo de cita
     */
    @RabbitListener(queues = RabbitConfig.REJECTED_QUEUE_NAME)
    public void onAppointmentRejected(AppointmentRejectedEvent event) {
        notificationService.sendAllChannels(event);
    }
}
