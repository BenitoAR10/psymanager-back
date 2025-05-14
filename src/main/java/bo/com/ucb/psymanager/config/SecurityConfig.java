package bo.com.ucb.psymanager.config;

import bo.com.ucb.psymanager.bl.OAuth2AuthenticationBl;
import bo.com.ucb.psymanager.util.CustomOAuth2User;
import bo.com.ucb.psymanager.util.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configura la seguridad de la aplicación, incluyendo reglas de autorización,
 * login OAuth2 con Google y filtro JWT.
 */
@Configuration
public class SecurityConfig {

    private final OAuth2AuthenticationBl oAuth2AuthenticationBl;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(OAuth2AuthenticationBl oAuth2AuthenticationBl, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.oAuth2AuthenticationBl = oAuth2AuthenticationBl;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Configura el filtro de seguridad principal.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers(
                                "/",
                                "/api/auth/token/refresh",
                                "/api/auth/me",
                                "/auth/login",
                                "/auth/success"
                        ).permitAll()

                        // Endpoints por rol
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/dashboard/**", "/api/therapists").hasRole("THERAPIST")

                        // Todos los demás requieren autenticación
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, ex1) ->
                                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No estás autenticado"))
                        .accessDeniedHandler((req, res, ex2) ->
                                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado"))
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2AuthenticationBl))
                        .successHandler((request, response, authentication) -> {
                            String redirectUrl;
                            if (authentication instanceof OAuth2AuthenticationToken oauth2Token) {
                                String registrationId = oauth2Token.getAuthorizedClientRegistrationId();
                                CustomOAuth2User user = (CustomOAuth2User) oauth2Token.getPrincipal();

                                // Diferenciar entre cliente móvil y web
                                redirectUrl = switch (registrationId) {
                                    case "google-mobile" -> "http://localhost:19006/auth/success?accessToken=" + user.getAccessToken()
                                            + "&refreshToken=" + user.getRefreshToken();
                                    case "google" -> "http://localhost:5173/auth/success?accessToken=" + user.getAccessToken()
                                            + "&refreshToken=" + user.getRefreshToken();
                                    default -> "http://localhost:5173/auth/error";
                                };
                            } else {
                                redirectUrl = "http://localhost:5173/auth/error";
                            }

                            response.sendRedirect(redirectUrl);
                        })
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
