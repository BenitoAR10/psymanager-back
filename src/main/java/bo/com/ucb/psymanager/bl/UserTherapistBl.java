package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.UserTherapistDao;
import bo.com.ucb.psymanager.dto.TherapistResponseDto;
import bo.com.ucb.psymanager.entities.UserTherapist;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserTherapistBl {

    private static final Logger logger = Logger.getLogger(UserTherapistBl.class);

    private final UserTherapistDao userTherapistDao;

    @Autowired
    public UserTherapistBl(UserTherapistDao userTherapistDao){
        this.userTherapistDao = userTherapistDao;
    }

    /**
     * Retorna la lista de todos los terapeutas registrados en el sistema.
     *
     * @return Lista de TherapistResponseDto con datos de los terapeutas.
     */
    public List<TherapistResponseDto> getAllTherapists(){
        logger.info("Solicitando lista de todos los terapeutas registrados.");

        List<UserTherapist> userTherapists = userTherapistDao.findAll();

        logger.debug("Cantidad de terapeutas encontrados: " + userTherapists.size());

        return userTherapists.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Mapea la entidad UserTherapist a TherapistResponseDto.
     *
     * @param ut Objeto UserTherapist a mapear.
     * @return DTO con los datos del terapeuta.
     */
    private TherapistResponseDto mapToDto(UserTherapist ut){
        TherapistResponseDto dto = new TherapistResponseDto(
                ut.getUser().getUserId(),
                ut.getUser().getFirstName(),
                ut.getUser().getLastName(),
                ut.getUser().getEmail()
        );

        logger.debug("Mapeando UserTherapist a DTO: " + dto.getFirstName() + " " + dto.getLastName());
        return dto;
    }
}