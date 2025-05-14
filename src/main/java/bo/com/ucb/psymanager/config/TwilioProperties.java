package bo.com.ucb.psymanager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

/**
 * Carga de propiedades Twilio desde el archivo application.yml.
 * Estas propiedades se inyectan automáticamente al iniciar la aplicación.
 */
@Configuration
@ConfigurationProperties(prefix = "twilio")
@Validated
@Data
public class TwilioProperties {

    /** Account SID de Twilio. */
    private String accountSid;

    /** Auth Token asociado al Account SID. */
    private String authToken;

    /** Número de WhatsApp registrado en Twilio. */
    private String whatsappFrom;

    /**
     * Identificadores de plantillas de mensaje de WhatsApp.
     * Se espera una estructura como:
     * twilio.template-sids:
     *   appointment-confirmation: "ABC123"
     *   appointment-reminder: "XYZ789"
     */
    private Map<String, String> templateSids;
}
