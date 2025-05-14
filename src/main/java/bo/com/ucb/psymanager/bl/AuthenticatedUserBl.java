package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.UserDao;
import bo.com.ucb.psymanager.dao.UserRoleDao;
import bo.com.ucb.psymanager.dto.AuthResponseDto;
import bo.com.ucb.psymanager.dto.CompleteProfileRequestDto;
import bo.com.ucb.psymanager.entities.User;
import bo.com.ucb.psymanager.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthenticatedUserBl {

    private static final Logger logger = Logger.getLogger(AuthenticatedUserBl.class);

    private final UserDao userDao;
    private final UserRoleDao userRoleDao;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticatedUserBl(UserDao userDao, UserRoleDao userRoleDao, JwtUtil jwtUtil) {
        this.userDao = userDao;
        this.userRoleDao = userRoleDao;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Obtiene el usuario autenticado basado en el token JWT.
     *
     * @param token JWT de autenticación.
     * @return Usuario autenticado si existe.
     */
    public Optional<User> getAuthenticatedUser(String token) {
        String email = jwtUtil.extractUsername(token);
        logger.debug("Extrayendo usuario autenticado desde token: " + email);
        return userDao.findByEmail(email);
    }

    /**
     * Obtiene los roles asociados a un usuario basado en su email.
     *
     * @param email Email del usuario.
     * @return Lista de roles.
     */
    public List<String> getUserRoles(String email) {
        logger.debug("Obteniendo roles del usuario con email: " + email);
        return userDao.findByEmail(email)
                .map(user -> userRoleDao.findByUser(user).stream()
                        .map(userRole -> userRole.getRole().getRole())
                        .collect(Collectors.toList()))
                .orElseGet(() -> {
                    logger.warn("Usuario no encontrado al obtener roles: " + email);
                    return Collections.emptyList();
                });
    }

    /**
     * Verifica si el usuario tiene un rol específico.
     *
     * @param email    Email del usuario.
     * @param roleName Nombre del rol a verificar.
     * @return `true` si tiene el rol, `false` en caso contrario.
     */
    public boolean hasRole(String email, String roleName) {
        boolean result = getUserRoles(email).contains(roleName);
        logger.debug("Verificando si el usuario " + email + " tiene el rol '" + roleName + "': " + result);
        return result;
    }

    /**
     * Genera un nuevo par de tokens JWT (accessToken y refreshToken) para el usuario.
     *
     * @param email Email del usuario.
     * @return Objeto AuthResponseDto que contiene el accessToken (con roles, firstName y lastName incluidos)
     *         y el refreshToken.
     */
    public AuthResponseDto generateTokens(String email) {
        logger.info("Generando tokens para usuario: " + email);
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Usuario no encontrado al generar tokens: " + email);
                    return new RuntimeException("User not found");
                });

        List<String> roles = getUserRoles(email);
        logger.debug("Roles encontrados: " + roles);

        return new AuthResponseDto(
                jwtUtil.generateAccessToken(email, roles, user.getFirstName(), user.getLastName(), user.getUserId()),
                jwtUtil.generateRefreshToken(email, roles, user.getFirstName(), user.getLastName(), user.getUserId())
        );
    }

    /**
     * Refresca el accessToken utilizando el refreshToken.
     *
     * @param refreshToken Refresh token del usuario.
     * @return Optional que contiene AuthResponseDto con los nuevos tokens si el refreshToken es válido,
     *         o vacío si no es válido.
     */
    public Optional<AuthResponseDto> refreshAccessToken(String refreshToken) {
        String email = jwtUtil.extractUsername(refreshToken);
        logger.info("Intentando refrescar token para usuario: " + email);

        if (email == null) {
            logger.warn("No se pudo extraer email del refresh token");
            return Optional.empty();
        }

        boolean userExists = userDao.findByEmail(email).isPresent();
        boolean tokenValid = jwtUtil.validateToken(refreshToken, email);

        if (!userExists || !tokenValid) {
            logger.warn("Refresh token inválido para email: " + email);
            return Optional.empty();
        }

        logger.info("Refresh token válido. Generando nuevos tokens para: " + email);
        return Optional.of(generateTokens(email));
    }

    /**
     * Completa el perfil de un usuario existente utilizando los datos proporcionados
     * en el DTO CompleteProfileRequestDto Este método se usa después de la autenticación
     * con Google, donde solo se registran nombre, apellido y correo inicialmente.
     *
     * @param email    Correo electrónico del usuario autenticado.
     * @param dto      DTO con los campos faltantes del perfil a completar.
     * @throws RuntimeException si el usuario no existe en la base de datos.
     */
    @Transactional
    public void completeUserProfile(String email, CompleteProfileRequestDto dto) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setPhoneNumber(dto.getPhoneNumber());
        user.setBirthDate(dto.getBirthDate());
        user.setBirthGender(dto.getBirthGender());
        user.setIdentityGender(dto.getIdentityGender());
        user.setAddress(dto.getAddress());
        user.setCiNumber(dto.getCiNumber());
        user.setCiComplement(dto.getCiComplement());
        user.setCiExtension(dto.getCiExtension());

        userDao.save(user);
    }

    /**
     * Obtiene un usuario completo desde la base de datos utilizando su correo electrónico.
     *
     * @param email Correo electrónico del usuario.
     * @return Objeto {@link User} correspondiente al email proporcionado.
     * @throws RuntimeException si no se encuentra ningún usuario con ese email.
     */
    public User getUserByEmail(String email) {
        logger.debug("Buscando usuario con email: " + email);
        return userDao.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Usuario no encontrado con email: " + email);
                    return new RuntimeException("Usuario no encontrado");
                });
    }

}
