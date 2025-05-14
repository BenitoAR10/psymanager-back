package bo.com.ucb.psymanager.util;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

/**
 * Representa a un usuario autenticado mediante OAuth2, extendiendo DefaultOAuth2User
 * para incluir el accessToken, refreshToken y roles.
 */
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private final String accessToken;
    private final String refreshToken;
    private final List<String> roles;

    /**
     * Crea un usuario OAuth2 personalizado con información adicional.
     *
     * @param oAuth2User    Usuario base proporcionado por Spring Security.
     * @param accessToken   Token de acceso emitido tras autenticación.
     * @param refreshToken  Token de actualización para renovar el acceso.
     * @param roles         Lista de roles asociados al usuario.
     */
    public CustomOAuth2User(OAuth2User oAuth2User, String accessToken, String refreshToken, List<String> roles) {
        super(oAuth2User.getAuthorities(), oAuth2User.getAttributes(), "email");
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.roles = roles;
    }

    /**
     * Obtiene el correo electrónico del usuario desde los atributos del token OAuth.
     *
     * @return email del usuario.
     */
    public String getEmail() {
        return (String) getAttributes().get("email");
    }
}