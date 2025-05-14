package bo.com.ucb.psymanager.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Configuración de RabbitMQ para manejar eventos relacionados a citas:
 * incluye exchange tipo topic, colas específicas y bindings con sus respectivas routing keys.
 */
@Configuration
public class RabbitConfig {

    // ======= Constantes de nombres =======
    public static final String EXCHANGE_NAME = "appointment.exchange";

    public static final String BOOKED_QUEUE_NAME = "appointment.booked";
    public static final String REMINDER_QUEUE_NAME = "appointment.reminder";
    public static final String REMINDER_DELAY_QUEUE_NAME = "appointment.reminder.delay";
    public static final String REJECTED_QUEUE_NAME = "appointment.rejected";

    public static final String ROUTING_KEY_BOOKED = "appointment.booked";
    public static final String ROUTING_KEY_REMINDER = "appointment.reminder";
    public static final String ROUTING_KEY_REJECTED = "appointment.rejected";

    // ======= Exchange =======

    /** Exchange tipo topic para eventos de citas. */
    @Bean
    public TopicExchange appointmentsExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // ======= Queues =======

    /** Cola para eventos de citas reservadas. */
    @Bean
    public Queue bookedQueue() {
        return new Queue(BOOKED_QUEUE_NAME, true);
    }

    /** Cola final para recordatorios de citas. */
    @Bean
    public Queue reminderQueue() {
        return new Queue(REMINDER_QUEUE_NAME, true);
    }

    /** Cola intermedia con TTL para generar recordatorios. */
    @Bean
    public Queue reminderDelayQueue() {
        return new Queue(
                REMINDER_DELAY_QUEUE_NAME,
                true,
                false,
                false,
                Map.of(
                        "x-dead-letter-exchange", EXCHANGE_NAME,
                        "x-dead-letter-routing-key", ROUTING_KEY_REMINDER
                )
        );
    }

    /** Cola para citas rechazadas. */
    @Bean
    public Queue rejectedQueue() {
        return new Queue(REJECTED_QUEUE_NAME, true);
    }

    // ======= Bindings =======

    @Bean
    public Binding bindingBooked(Queue bookedQueue, TopicExchange appointmentsExchange) {
        return BindingBuilder.bind(bookedQueue)
                .to(appointmentsExchange)
                .with(ROUTING_KEY_BOOKED);
    }

    @Bean
    public Binding bindingReminder(Queue reminderQueue, TopicExchange appointmentsExchange) {
        return BindingBuilder.bind(reminderQueue)
                .to(appointmentsExchange)
                .with(ROUTING_KEY_REMINDER);
    }

    @Bean
    public Binding bindingRejected(Queue rejectedQueue, TopicExchange appointmentsExchange) {
        return BindingBuilder.bind(rejectedQueue)
                .to(appointmentsExchange)
                .with(ROUTING_KEY_REJECTED);
    }

    // ======= Conversor y plantilla =======

    /** Conversor JSON para mensajes con Jackson. */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /** RabbitTemplate con soporte para mensajes JSON. */
    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter
    ) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }
}
