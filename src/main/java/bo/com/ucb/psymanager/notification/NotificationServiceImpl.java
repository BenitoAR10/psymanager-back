package bo.com.ucb.psymanager.notification;

import bo.com.ucb.psymanager.config.TwilioProperties;
import bo.com.ucb.psymanager.events.AppointmentBookedEvent;
import bo.com.ucb.psymanager.events.AppointmentRejectedEvent;
import bo.com.ucb.psymanager.events.AppointmentReminderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio centralizado para el envío de notificaciones multicanal.
 * Actualmente implementa solo WhatsApp vía Twilio, usando plantillas definidas.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final TwilioNotificationService twilioService;
    private final TwilioProperties props;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Notifica al paciente que su cita fue confirmada.
     */
    @Override
    public void sendAllChannels(AppointmentBookedEvent event) {
        TemplateData data = prepareTemplateData(event.dateTime(), event.therapistName(), event.patientPhone());
        String sid = props.getTemplateSids().get("appointment-confirmation");
        sendWhatsApp(data, sid, event.appointmentId(), "CONFIRMACIÓN");
    }

    /**
     * Notifica al paciente que su cita fue rechazada.
     */
    @Override
    public void sendAllChannels(AppointmentRejectedEvent event) {
        TemplateData data = prepareTemplateData(event.dateTime(), event.therapistName(), event.patientPhone());
        String sid = props.getTemplateSids().get("appointment-rejection");
        sendWhatsApp(data, sid, event.appointmentId(), "RECHAZO");
    }

    /**
     * Envía un recordatorio programado antes de la cita.
     */
    @Override
    public void sendAllChannels(AppointmentReminderEvent event) {
        Map<String, String> vars = new HashMap<>();
        vars.put("1", event.appointmentTime().format(DATE_FMT));
        vars.put("2", event.reminderTime().format(TIME_FMT));
        vars.put("3", event.therapistName());

        String to = event.patientPhone();
        String sid = props.getTemplateSids().get("appointment-reminder");

        try {
            twilioService.sendWhatsAppTemplate(to, sid, vars);
            log.info("Recordatorio WhatsApp enviado a {} para cita {}", to, event.appointmentId());
        } catch (Exception ex) {
            log.error("Error enviando recordatorio WhatsApp a {}: {}", to, ex.getMessage(), ex);
        }
    }

    /**
     * Método auxiliar para envío de WhatsApp con manejo de errores.
     *
     * @param data datos empaquetados para envío
     * @param sid ID de plantilla Twilio
     * @param appointmentId ID de la cita
     * @param type tipo de evento ("CONFIRMACIÓN", "RECHAZO", etc.)
     */
    private void sendWhatsApp(TemplateData data, String sid, Long appointmentId, String type) {
        try {
            twilioService.sendWhatsAppTemplate(data.to, sid, data.vars);
            log.info("Notificación WhatsApp de {} enviada a {} para cita {}", type, data.to, appointmentId);
        } catch (Exception ex) {
            log.error("Error enviando WhatsApp de {} a {}: {}", type, data.to, ex.getMessage(), ex);
        }
    }

    /**
     * Prepara los valores para la plantilla de notificación.
     *
     * @param dateTime fecha y hora de la cita
     * @param therapistName nombre del terapeuta
     * @param patientPhone número de teléfono del paciente
     * @return TemplateData con destino y variables formateadas
     */
    private TemplateData prepareTemplateData(LocalDateTime dateTime, String therapistName, String patientPhone) {
        Map<String, String> vars = new HashMap<>();
        vars.put("1", dateTime.format(DATE_FMT));
        vars.put("2", dateTime.format(TIME_FMT));
        vars.put("3", therapistName);
        return new TemplateData(patientPhone, vars);
    }

    /**
     * Contenedor simple para empaquetar el destinatario y los valores de plantilla.
     */
    private static class TemplateData {
        final String to;
        final Map<String, String> vars;

        TemplateData(String to, Map<String, String> vars) {
            this.to = to;
            this.vars = vars;
        }
    }
}
