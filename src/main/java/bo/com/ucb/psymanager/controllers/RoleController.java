package bo.com.ucb.psymanager.controllers;

import bo.com.ucb.psymanager.dto.AssignRoleRequestDto;
import bo.com.ucb.psymanager.bl.RoleBl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin/roles")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "http://localhost:5173")
public class RoleController {

    private static final Logger logger = Logger.getLogger(RoleController.class);

    private final RoleBl roleBl;

    @Autowired
    public RoleController(RoleBl roleBl) {
        this.roleBl = roleBl;
    }

    /**
     * Asigna un rol a un usuario especificado por email.
     *
     * @param request Objeto con el email del usuario y el nombre del rol.
     * @return Mensaje de éxito o error.
     */
    @PostMapping("/assign")
    public ResponseEntity<?> assignRole(@RequestBody AssignRoleRequestDto request) {
        logger.info("Solicitud POST a /api/admin/roles/assign para el usuario: " + request.getEmail());

        if (request.getEmail() == null || request.getRole() == null) {
            logger.warn("Datos incompletos para asignación de rol: " + request);
            return ResponseEntity.badRequest().body("Email and role are required");
        }

        roleBl.assignRoleToUser(request.getEmail(), request.getRole());
        logger.info("Rol '" + request.getRole() + "' asignado exitosamente a " + request.getEmail());

        return ResponseEntity.ok("Role assigned successfully");
    }
}