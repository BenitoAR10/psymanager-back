package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.TherapistSpecialty;
import bo.com.ucb.psymanager.entities.UserTherapist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para acceder a las especialidades asociadas a cada terapeuta.
 * Permite listar, agregar y eliminar asignaciones entre terapeutas y especialidades.
 */
@Repository
public interface TherapistSpecialtyDao extends JpaRepository<TherapistSpecialty, Integer> {

    /**
     * Busca todas las especialidades asignadas a un terapeuta.
     *
     * @param userTherapist terapeuta autenticado
     * @return lista de especialidades asociadas
     */
    List<TherapistSpecialty> findByUserTherapist(UserTherapist userTherapist);

    /**
     * Elimina todas las especialidades de un terapeuta (útil para re-asignar).
     *
     * @param userTherapist terapeuta del que se eliminarán las relaciones
     */
    void deleteByUserTherapist(UserTherapist userTherapist);
}