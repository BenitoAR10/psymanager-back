package bo.com.ucb.psymanager.dao;

import bo.com.ucb.psymanager.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionDao extends JpaRepository<Permission, Long> {
    Optional<Permission> findByPermissionName(String permissionName);
}
