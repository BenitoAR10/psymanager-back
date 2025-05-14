package bo.com.ucb.psymanager.events;

import bo.com.ucb.psymanager.config.RabbitConfig;
import bo.com.ucb.psymanager.notification.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumidor de eventos de citas reservadas.
 * Escucha mensajes desde la cola de RabbitMQ y los redirige al servicio de notificación.
 */
@Component
public class AppointmentBookedConsumer {

    private final NotificationService notificationService;

    public AppointmentBookedConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Método que se activa cuando se recibe un evento de cita reservada en la cola correspondiente.
     *
     * @param event evento de cita reservada
     */
    @RabbitListener(queues = RabbitConfig.BOOKED_QUEUE_NAME)
    public void onAppointmentBooked(AppointmentBookedEvent event) {
        notificationService.sendAllChannels(event);
    }
}
