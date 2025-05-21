package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.WellnessExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO para acceder a los ejercicios de bienestar emocional (WellnessExercise).
 * Proporciona métodos para recuperar ejercicios disponibles por categoría.
 */
@Repository
public interface WellnessExerciseDao extends JpaRepository<WellnessExercise, Long> {

    /**
     * Obtiene la lista de ejercicios por categoría específica.
     *
     * @param category Categoría del ejercicio (por ejemplo: "Ansiedad", "Estrés").
     * @return Lista de ejercicios que pertenecen a la categoría indicada.
     */
    List<WellnessExercise> findByCategory(String category);
}