package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.CompletedExercise;
import bo.com.ucb.psymanager.entities.UserPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO para acceder a los registros de ejercicios completados por los estudiantes.
 * Permite obtener el historial de ejercicios realizados por cada paciente.
 */
@Repository
public interface CompletedExerciseDao extends JpaRepository<CompletedExercise, Long> {

    /**
     * Obtiene todos los ejercicios completados por un paciente.
     *
     * @param userPatient El paciente (estudiante) cuya actividad se desea consultar.
     * @return Lista de ejercicios completados por el usuario.
     */
    List<CompletedExercise> findByUserPatient(UserPatient userPatient);
}