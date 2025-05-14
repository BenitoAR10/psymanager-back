package bo.com.ucb.psymanager.controllers;


import bo.com.ucb.psymanager.bl.UserTherapistBl;
import bo.com.ucb.psymanager.dto.TherapistResponseDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/therapists")
@CrossOrigin(origins = "http://localhost:5173")
public class TherapistController {

    private static final Logger logger = Logger.getLogger(TherapistController.class);

    private final UserTherapistBl userTherapistBl;

    @Autowired
    public TherapistController(UserTherapistBl userTherapistBl){
        this.userTherapistBl = userTherapistBl;
    }

    /**
     * Obtiene la lista de todos los terapeutas registrados.
     *
     * @return Lista de terapeutas.
     */
    @GetMapping
    public ResponseEntity<List<TherapistResponseDto>> getAllTherapists(){
        logger.info("Solicitud GET a /api/therapists");
        List<TherapistResponseDto> therapist = userTherapistBl.getAllTherapists();
        logger.debug("Cantidad de terapeutas encontrados: " + therapist.size());
        return ResponseEntity.ok(therapist);
    }
}
