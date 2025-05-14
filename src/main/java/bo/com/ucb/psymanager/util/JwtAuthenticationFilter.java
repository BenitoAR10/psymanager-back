package bo.com.ucb.psymanager.util;

import bo.com.ucb.psymanager.dao.UserDao;
import bo.com.ucb.psymanager.dao.UserRoleDao;
import bo.com.ucb.psymanager.entities.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Filtro personalizado que intercepta cada petición entrante
 * para validar y procesar tokens JWT.
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtService;
    private final UserDao userDao;
    private final UserRoleDao userRoleDao;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtService, UserDao userDao, UserRoleDao userRoleDao) {
        this.jwtService = jwtService;
        this.userDao = userDao;
        this.userRoleDao = userRoleDao;
    }

    /**
     * Intercepta cada solicitud HTTP para validar el token JWT.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        // Ignorar rutas públicas
        if (
                path.equals("/api/auth/token/refresh") ||
                        path.equals("/auth/login") ||
                        path.equals("/auth/success") ||
                        path.equals("/") ||
                        path.startsWith("/oauth2")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        // Si no hay token o no es Bearer, continuar sin autenticación
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);

        // Validar token solo si aún no hay autenticación
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<User> userOptional = userDao.findByEmail(email);

            if (userOptional.isPresent() && jwtService.validateToken(token, email)) {
                User user = userOptional.get();

                List<SimpleGrantedAuthority> authorities = userRoleDao.findByUser(user).stream()
                        .map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.getRole().getRole()))
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        email, null, authorities
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("Autenticación establecida para usuario: {}", email);
            } else {
                log.warn("Token inválido o usuario no encontrado: {}", email);
            }
        }

        filterChain.doFilter(request, response);
    }
}
