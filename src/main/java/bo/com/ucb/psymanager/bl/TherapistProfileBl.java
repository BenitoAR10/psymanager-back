package bo.com.ucb.psymanager.bl;


import bo.com.ucb.psymanager.dao.SpecialtyDao;
import bo.com.ucb.psymanager.dao.TherapistSpecialtyDao;
import bo.com.ucb.psymanager.dao.UserDao;
import bo.com.ucb.psymanager.dao.UserTherapistDao;
import bo.com.ucb.psymanager.dto.TherapistProfileUpdateDto;
import bo.com.ucb.psymanager.dto.TherapistProfileViewDto;
import bo.com.ucb.psymanager.entities.Specialty;
import bo.com.ucb.psymanager.entities.TherapistSpecialty;
import bo.com.ucb.psymanager.entities.User;
import bo.com.ucb.psymanager.entities.UserTherapist;
import jakarta.transaction.Transactional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TherapistProfileBl {

    private static final Logger logger = Logger.getLogger(TherapistProfileBl.class);

    private final UserDao userDao;
    private final UserTherapistDao userTherapistDao;
    private final SpecialtyDao specialtyDao;
    private final TherapistSpecialtyDao therapistSpecialtyDao;

    @Autowired
    public TherapistProfileBl(
            UserDao userDao,
            UserTherapistDao userTherapistDao,
            SpecialtyDao specialtyDao,
            TherapistSpecialtyDao therapistSpecialtyDao
    ) {
        this.userDao = userDao;
        this.userTherapistDao = userTherapistDao;
        this.specialtyDao = specialtyDao;
        this.therapistSpecialtyDao = therapistSpecialtyDao;
    }

    /**
     * Completa el perfil del terapeuta autenticado actualizando datos personales
     * y reemplazando sus especialidades actuales.
     *
     * @param email Email del terapeuta autenticado
     * @param dto   Datos del perfil a actualizar
     */
    @Transactional
    public void updateTherapistProfile(String email, TherapistProfileUpdateDto dto) {
        logger.info("Actualizando perfil del terapeuta con email: " + email);

        // Buscar usuario y entidad de terapeuta
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("No se encontró el usuario con email: {}"+ email);
                    return new RuntimeException("Usuario no encontrado");
                });

        UserTherapist userTherapist = userTherapistDao.findById(user.getUserId())
                .orElseThrow(() -> {
                    logger.error("No se encontró entidad UserTherapist para userId: {}"+ user.getUserId());
                    return new RuntimeException("Usuario no es terapeuta");
                });

        // Actualizar datos personales
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setCiNumber(dto.getCiNumber());
        user.setCiComplement(dto.getCiComplement());
        user.setCiExtension(dto.getCiExtension());
        userDao.save(user);
        logger.info("Datos personales actualizados para terapeuta: {}"+ email);

        // Reemplazar especialidades
        therapistSpecialtyDao.deleteByUserTherapist(userTherapist);
        for (Integer specialtyId : dto.getSpecialtyIds()) {
            Specialty specialty = specialtyDao.findById(specialtyId)
                    .orElseThrow(() -> {
                        logger.error("Especialidad no encontrada con ID: {}"+ specialtyId);
                        return new RuntimeException("Especialidad no válida: " + specialtyId);
                    });
            TherapistSpecialty ts = new TherapistSpecialty();
            ts.setUserTherapist(userTherapist);
            ts.setSpecialty(specialty);
            therapistSpecialtyDao.save(ts);
        }
        logger.info("Especialidades asignadas para terapeuta con ID: {}"+ user.getUserId());
    }



    /**
     * Obtiene los datos del perfil del terapeuta autenticado.
     *
     * @param email correo electrónico del terapeuta (extraído del JWT)
     * @return DTO con los datos del perfil del terapeuta
     * @throws RuntimeException si el usuario o el terapeuta no existen
     */
    public TherapistProfileViewDto getTherapistProfile(String email) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));

        UserTherapist userTherapist = userTherapistDao.findById(user.getUserId())
                .orElseThrow(() -> new RuntimeException("No se encontró terapeuta para el usuario ID: " + user.getUserId()));

        List<TherapistSpecialty> specialties = therapistSpecialtyDao.findByUserTherapist(userTherapist);

        List<TherapistProfileViewDto.SpecialtyDto> specialtyDtos = specialties.stream()
                .map(ts -> new TherapistProfileViewDto.SpecialtyDto(
                        ts.getSpecialty().getSpecialtyId(),
                        ts.getSpecialty().getSpecialtyName()
                ))
                .toList();

        return new TherapistProfileViewDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                Optional.ofNullable(user.getPhoneNumber()).orElse(""),
                Optional.ofNullable(user.getAddress()).orElse(""),
                Optional.ofNullable(user.getCiNumber()).orElse(""),
                Optional.ofNullable(user.getCiComplement()).orElse(""),
                Optional.ofNullable(user.getCiExtension()).orElse(""),
                specialtyDtos
        );

    }
}