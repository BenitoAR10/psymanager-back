package bo.com.ucb.psymanager.events;


import bo.com.ucb.psymanager.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Publicador de eventos a través de RabbitMQ.
 * Encapsula la lógica de envío para eventos de cita confirmada y recordatorios,
 * con soporte para mensajes retardados usando TTL en cola intermedia.
 */
@Component
@RequiredArgsConstructor
public class AmqpEventPublisher {


    private final RabbitTemplate rabbitTemplate;

    /**
     * Publica un evento de cita confirmada en el exchange principal.
     *
     * @param event evento {@link AppointmentBookedEvent} a enviar.
     */
    public void publishBooked(AppointmentBookedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NAME,
                RabbitConfig.ROUTING_KEY_BOOKED,
                event
        );
    }

    /**
     * Publica un evento de recordatorio de cita de forma inmediata
     * en el exchange principal (sin retraso).
     *
     * @param event evento {@link AppointmentReminderEvent} a enviar.
     */
    public void publishReminder(AppointmentReminderEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NAME,
                RabbitConfig.ROUTING_KEY_REMINDER,
                event
        );
    }

    /**
     * Publica un evento cuando una cita es rechazada.
     *
     * @param event evento de cita rechazada.
     */
    public void publishRejected(AppointmentRejectedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NAME,
                RabbitConfig.ROUTING_KEY_REJECTED,
                event
        );
    }

    /**
     * Publica un evento de recordatorio con retraso usando la cola intermedia TTL:
     * - Se envía al default exchange (""), usando el nombre de la cola delay como routing key.
     * - El header "expiration" define el TTL hasta que el mensaje expire.
     * - Al expirar, RabbitMQ reencola el mensaje al exchange principal con la routingKey de recordatorio.
     *
     * @param event   evento {@link AppointmentReminderEvent} a retrasar.
     * @param delayMs tiempo en milisegundos antes de reenviar a la cola final.
     */
    public void publishReminderDelayed(AppointmentReminderEvent event, long delayMs) {
        rabbitTemplate.convertAndSend(
                "", // default exchange
                RabbitConfig.REMINDER_DELAY_QUEUE_NAME,
                event,
                message -> {
                    message.getMessageProperties().setExpiration(String.valueOf(delayMs));
                    return message;
                }
        );
    }
}
