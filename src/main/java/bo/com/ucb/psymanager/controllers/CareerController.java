package bo.com.ucb.psymanager.controllers;


import bo.com.ucb.psymanager.bl.CareerBl;
import bo.com.ucb.psymanager.dto.CareerSimpleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Controlador para exponer operaciones relacionadas con carreras y facultades.
 */
@RestController
@RequestMapping("/api/careers")
public class CareerController {

    private static final Logger logger = LoggerFactory.getLogger(CareerController.class);
    private final CareerBl careerBl;

    public CareerController(CareerBl careerBl) {
        this.careerBl = careerBl;
    }

    /**
     * Obtiene todas las facultades que tienen carreras activas registradas.
     *
     * @return Lista de nombres de facultades en formato JSON.
     */
    @GetMapping("/faculties")
    public ResponseEntity<List<String>> getFaculties() {
        logger.info("Solicitud GET a /api/careers/faculties");
        List<String> faculties = careerBl.getAllFaculties();
        return ResponseEntity.ok(faculties);
    }

    /**
     * Obtiene todas las carreras activas asociadas a una facultad específica.
     *
     * @param faculty Nombre exacto de la facultad
     * @return Lista de nombres de carreras
     */
    @GetMapping("/by-faculty")
    public ResponseEntity<List<CareerSimpleDto>> getCareersByFaculty(@RequestParam String faculty) {
        logger.info("Solicitud GET a /api/careers/by-faculty con facultad={}", faculty);
        List<CareerSimpleDto> careers = careerBl.getCareerDtosByFaculty(faculty);
        return ResponseEntity.ok(careers);
    }


}