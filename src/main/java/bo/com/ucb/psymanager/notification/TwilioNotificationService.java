package bo.com.ucb.psymanager.notification;

import bo.com.ucb.psymanager.config.TwilioProperties;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Servicio que envia mensajes de WhatsApp usando Twilio templates.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TwilioNotificationService {

    private final TwilioProperties props;
    private final TwilioRestClient twilioClient;

    /**
     * Envía un mensaje de plantilla de WhatsApp usando Twilio.
     *
     * @param to               Número de teléfono destino (formato "whatsapp:+591…")
     * @param templateSid      ContentSid de la plantilla pre-aprobada
     * @param contentVariables Mapa de variables {"1":"05/05","2":"15:30"}
     */
    public void sendWhatsAppTemplate(
            String to,
            String templateSid,
            Map<String, String> contentVariables
    ) {
        // Convertimos el map a JSON string
        String varsJson = new JSONObject(contentVariables).toString();

        try {
            Message message = Message
                    .creator(
                            new PhoneNumber(to),
                            new PhoneNumber(props.getWhatsappFrom()),
                            // Body obligatorio en esta sobrecarga, pero vacio si usamos plantilla
                            ""
                    )
                    .setContentSid(templateSid)
                    .setContentVariables(varsJson)
                    .create(twilioClient);

            log.info("WhatsApp template enviado (sid={}) a {}", message.getSid(), to);
        } catch (Exception ex) {
            log.error("Error enviando WhatsApp template a {}: {}", to, ex.getMessage(), ex);
            // aquí podrías relanzar una excepción custom o un evento de retry
        }
    }
}