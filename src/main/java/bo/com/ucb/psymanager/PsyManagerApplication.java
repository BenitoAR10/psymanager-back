package bo.com.ucb.psymanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class PsyManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PsyManagerApplication.class, args);
	}

}
