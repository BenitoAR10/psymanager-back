package bo.com.ucb.psymanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuración global de CORS para permitir el acceso entre dominios
 * desde clientes web y móviles durante el desarrollo.
 */
@Configuration
public class CorsConfig {

    /**
     * Define la configuración de CORS para permitir solicitudes desde
     * múltiples orígenes (web, móvil) y soportar métodos comunes HTTP.
     *
     * @return fuente de configuración CORS aplicable a todas las rutas.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "http://192.168.1.204:*",
                "https://*.ngrok-free.app"
        ));

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Headers permitidos (todos)
        configuration.setAllowedHeaders(List.of("*"));

        // Permitir envío de cookies/autenticación
        configuration.setAllowCredentials(true);

        // Aplicar configuración a todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
