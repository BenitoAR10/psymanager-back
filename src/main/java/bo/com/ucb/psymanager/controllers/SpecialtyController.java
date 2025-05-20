package bo.com.ucb.psymanager.controllers;

import bo.com.ucb.psymanager.dao.SpecialtyDao;
import bo.com.ucb.psymanager.dto.SpecialtyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/specialties")
public class SpecialtyController {

    private final SpecialtyDao specialtyDao;

    @Autowired
    public SpecialtyController(SpecialtyDao specialtyDao) {
        this.specialtyDao = specialtyDao;
    }

    /**
     * Devuelve la lista completa de especialidades disponibles.
     *
     * @return lista de especialidades
     */
    @GetMapping
    public List<SpecialtyDto> getAllSpecialties() {
        return specialtyDao.findAll().stream()
                .map(s -> new SpecialtyDto(s.getSpecialtyId(), s.getSpecialtyName()))
                .collect(Collectors.toList());
    }
}