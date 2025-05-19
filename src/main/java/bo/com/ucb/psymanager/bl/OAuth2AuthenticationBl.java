package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.RoleDao;
import bo.com.ucb.psymanager.dao.UserDao;
import bo.com.ucb.psymanager.dao.UserRoleDao;
import bo.com.ucb.psymanager.entities.Role;
import bo.com.ucb.psymanager.entities.User;
import bo.com.ucb.psymanager.entities.UserRole;
import bo.com.ucb.psymanager.util.CustomOAuth2User;
import bo.com.ucb.psymanager.util.JwtUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;


@Service
public class OAuth2AuthenticationBl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final Logger logger = Logger.getLogger(OAuth2AuthenticationBl.class);

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final UserRoleDao userRoleDao;
    private final JwtUtil jwtUtil;

    @Autowired
    public OAuth2AuthenticationBl(UserDao userDao, RoleDao roleDao, UserRoleDao userRoleDao, JwtUtil jwtUtil) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.userRoleDao = userRoleDao;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Procesa la autenticación OAuth2, crea usuario si no existe y genera tokens JWT.
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            throw new OAuth2AuthenticationException("No se pudo obtener el email del usuario de Google.");
        }


        logger.info("Autenticación OAuth2 iniciada para: " + email);

        User user = userDao.findByEmail(email)
                .orElseGet(() -> {
                    logger.info("Usuario no encontrado, creando nuevo usuario con email: " + email);
                    return createNewUser(oAuth2User);
                });

        List<String> roles = userRoleDao.findByUser(user).stream()
                .map(userRole -> userRole.getRole().getRole())
                .collect(Collectors.toList());

        logger.debug("Roles encontrados para " + email + ": " + roles);

        String accessToken = jwtUtil.generateAccessToken(email, roles, user.getFirstName(), user.getLastName(), user.getUserId());
        String refreshToken = jwtUtil.generateRefreshToken(email, roles, user.getFirstName(), user.getLastName(), user.getUserId());

        logger.info("Tokens generados exitosamente para " + email);

        return new CustomOAuth2User(oAuth2User, accessToken, refreshToken, roles);
    }

    /**
     * Crea un nuevo usuario en la base de datos con el rol por defecto "PATIENT".
     */
    private User createNewUser(OAuth2User oAuth2User) {
        User newUser = new User();
        newUser.setEmail(oAuth2User.getAttribute("email"));
        newUser.setFirstName(oAuth2User.getAttribute("given_name") != null ? oAuth2User.getAttribute("given_name") : "SinNombre");
        newUser.setLastName(oAuth2User.getAttribute("family_name") != null ? oAuth2User.getAttribute("family_name") : "SinApellido");
        userDao.save(newUser);

        Role defaultRole = roleDao.findByRole("PATIENT")
                .orElseThrow(() -> {
                    logger.error("Rol 'PATIENT' no encontrado");
                    return new RuntimeException("Role 'PATIENT' not found");
                });

        userRoleDao.save(new UserRole(newUser, defaultRole));
        logger.info("Nuevo usuario creado y rol 'PATIENT' asignado: " + newUser.getEmail());

        return newUser;
    }

}