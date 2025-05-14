package bo.com.ucb.psymanager.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Utilidad para generar y validar tokens JWT de acceso y actualización (refresh).
 */
@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.refreshExpiration}")
    private long refreshExpiration;

    /**
     * Log inicial para verificar que las propiedades fueron cargadas correctamente.
     */
    @PostConstruct
    public void init() {
        log.info("JWT configuration loaded: expiration={}ms, refreshExpiration={}ms", expiration, refreshExpiration);
    }

    /**
     * Genera un token de acceso JWT con la información básica del usuario.
     *
     * @param email     Correo electrónico del usuario.
     * @param roles     Lista de roles.
     * @param firstName Nombre del usuario.
     * @param lastName  Apellido del usuario.
     * @param userId    Identificador único del usuario.
     * @return Token JWT firmado.
     */
    public String generateAccessToken(String email, List<String> roles, String firstName, String lastName, Long userId) {
        return JWT.create()
                .withSubject(email)
                .withClaim("roles", roles)
                .withClaim("firstName", firstName)
                .withClaim("lastName", lastName)
                .withClaim("userId", userId)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(Algorithm.HMAC256(secretKey));
    }

    /**
     * Genera un refresh token JWT para renovar sesiones autenticadas.
     *
     * @param email     Correo electrónico del usuario.
     * @param roles     Lista de roles.
     * @param firstName Nombre del usuario.
     * @param lastName  Apellido del usuario.
     * @param userId    Identificador único del usuario.
     * @return Refresh token JWT firmado.
     */
    public String generateRefreshToken(String email, List<String> roles, String firstName, String lastName, Long userId) {
        return JWT.create()
                .withSubject(email)
                .withClaim("roles", roles)
                .withClaim("firstName", firstName)
                .withClaim("lastName", lastName)
                .withClaim("userId", userId)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshExpiration))
                .sign(Algorithm.HMAC256(secretKey));
    }

    /**
     * Extrae el correo electrónico (subject) desde un token JWT.
     *
     * @param token Token JWT.
     * @return Email extraído, o null si la verificación falla.
     */
    public String extractUsername(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secretKey))
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            log.warn("Error extrayendo email del token JWT: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Valida un token JWT comparando el email y su vigencia.
     *
     * @param token Token JWT.
     * @param email Email esperado.
     * @return true si es válido y corresponde al usuario, false en caso contrario.
     */
    public boolean validateToken(String token, String email) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            boolean isValid = email.equals(decodedJWT.getSubject()) && decodedJWT.getExpiresAt().after(new Date());
            if (!isValid) {
                log.warn("Token JWT inválido o expirado para usuario: {}", email);
            }
            return isValid;
        } catch (JWTVerificationException e) {
            log.warn("Error al validar token JWT: {}", e.getMessage());
            return false;
        }
    }
}
