package bo.com.ucb.psymanager.config;

import com.twilio.Twilio;
import com.twilio.http.TwilioRestClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de cliente Twilio y carga inicial de credenciales desde propiedades.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class TwilioConfig {

    private final TwilioProperties props;

    /**
     * Inicializa el SDK de Twilio con Account SID y Auth Token.
     */
    @PostConstruct
    public void initTwilioSdk() {
        log.info("Inicializando Twilio SDK (AccountSid={})", props.getAccountSid());
        Twilio.init(props.getAccountSid(), props.getAuthToken());
    }

    /**
     * Expone el cliente de Twilio como un bean reutilizable.
     *
     * @return instancia de TwilioRestClient.
     */
    @Bean
    public TwilioRestClient twilioRestClient() {
        return new TwilioRestClient
                .Builder(props.getAccountSid(), props.getAuthToken())
                .build();
    }
}
