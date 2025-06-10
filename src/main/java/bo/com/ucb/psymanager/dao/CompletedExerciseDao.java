package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.CompletedExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



/**
 * DAO para acceder a los registros de ejercicios completados por los estudiantes.
 * Permite obtener el historial de ejercicios realizados por cada paciente.
 */
@Repository
public interface CompletedExerciseDao extends JpaRepository<CompletedExercise, Long> {


}