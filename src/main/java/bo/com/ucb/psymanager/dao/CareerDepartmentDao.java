package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.CareerDepartment;
import bo.com.ucb.psymanager.entities.UserPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO para acceder a los registros de carrera académica de los pacientes.
 */
@Repository
public interface CareerDepartmentDao extends JpaRepository<CareerDepartment, Long> {

    /**
     * Obtiene todas las carreras asociadas a un paciente específico.
     *
     * @param userPatient Entidad UserPatient.
     * @return Lista de carreras asociadas.
     */
    List<CareerDepartment> findByUserPatient(UserPatient userPatient);

}