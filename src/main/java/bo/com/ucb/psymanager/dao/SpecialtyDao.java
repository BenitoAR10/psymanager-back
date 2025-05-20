package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para acceder a las especialidades registradas en el sistema.
 * Permite consultar, listar y buscar por nombre.
 */
@Repository
public interface SpecialtyDao extends JpaRepository<Specialty, Integer> {

    /**
     * Busca especialidades por nombre ignorando mayúsculas/minúsculas.
     *
     * @param name nombre (parcial o completo) de la especialidad
     * @return lista de especialidades coincidentes
     */
    List<Specialty> findBySpecialtyNameContainingIgnoreCase(String name);
}