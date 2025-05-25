package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.CareerDao;
import bo.com.ucb.psymanager.dto.CareerSimpleDto;
import bo.com.ucb.psymanager.entities.Career;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lógica de negocio para operaciones relacionadas con carreras universitarias.
 */
@Service
public class CareerBl {

    private static final Logger logger = LoggerFactory.getLogger(CareerBl.class);
    private final CareerDao careerDao;

    public CareerBl(CareerDao careerDao) {
        this.careerDao = careerDao;
    }

    /**
     * Obtiene una lista de nombres únicos de facultades que tienen carreras activas.
     *
     * @return Lista de nombres de facultades activas.
     */
    public List<String> getAllFaculties() {
        logger.info("Obteniendo lista de facultades únicas con carreras activas");
        return careerDao.findDistinctFaculties();
    }

    /**
     * Retorna los nombres de todas las carreras activas para una facultad específica.
     *
     * @param faculty Nombre de la facultad
     * @return Lista de nombres de carreras activas
     */
    public List<CareerSimpleDto> getCareerDtosByFaculty(String faculty) {
        logger.info("Buscando carreras activas para la facultad: {}", faculty);

        if (faculty == null || faculty.trim().isEmpty()) {
            logger.warn("Facultad no proporcionada o vacía.");
            throw new IllegalArgumentException("La facultad no puede estar vacía.");
        }

        return careerDao.findAllByFaculty(faculty.trim()).stream()
                .filter(c -> "Activo".equalsIgnoreCase(c.getStatus()))
                .map(c -> new CareerSimpleDto(c.getCareerId(), c.getCareerName()))
                .toList();
    }


}