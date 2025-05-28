package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.Career;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interfaz de acceso a datos para la entidad {@link Career}.
 * Proporciona métodos para interactuar con la tabla ps_career en la base de datos.
 */
@Repository
public interface CareerDao extends JpaRepository<Career, Long> {

    /**
     * Obtiene todas las carreras cuyo estado sea el proporcionado.
     *
     * @param status Estado de la carrera (ej. "Activo", "Inactivo", etc.)
     * @return Lista de carreras con el estado especificado.
     */
    List<Career> findAllByStatus(String status);

    /**
     * Obtiene todas las carreras asociadas a una facultad específica.
     *
     * @param faculty Nombre exacto de la facultad (ej. "Ciencias Sociales")
     * @return Lista de carreras dentro de esa facultad.
     */
    List<Career> findAllByFaculty(String faculty);
    /**
     * Obtiene una lista de nombres de facultades distintas de las carreras activas.
     *
     * @return Lista de nombres únicos de facultades.
     */
    @Query("SELECT DISTINCT c.faculty FROM Career c WHERE c.status = 'ACTIVO'")
    List<String> findDistinctFaculties();

}