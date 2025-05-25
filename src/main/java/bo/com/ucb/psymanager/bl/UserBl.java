package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.UserDao;
import bo.com.ucb.psymanager.dto.UserProfileUpdateDto;
import bo.com.ucb.psymanager.entities.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * Lógica de negocio relacionada con el perfil del usuario autenticado.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserBl {

    private final UserDao userDao;

    /**
     * Actualiza los datos personales del usuario identificado por su correo electrónico.
     *
     * @param email Correo del usuario autenticado.
     * @param dto Datos a actualizar.
     * @throws IllegalArgumentException si el usuario no existe.
     */
    @Transactional
    public void updateProfile(String email, UserProfileUpdateDto dto) {
        log.info("Actualizando perfil del usuario con email={}", email);

        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setIdentityGender(dto.getIdentityGender());

        userDao.save(user);
        log.info("Perfil actualizado correctamente para userId={}", user.getUserId());
    }
}