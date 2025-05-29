package bo.com.ucb.psymanager.controllers;

import bo.com.ucb.psymanager.bl.AuthenticatedUserBl;
import bo.com.ucb.psymanager.bl.PermissionBl;
import bo.com.ucb.psymanager.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


/**
 * Controlador que permite obtener los permisos efectivos del usuario autenticado.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class PermissionController {

    private final AuthenticatedUserBl authenticatedUserBl;
    private final PermissionBl permissionBl;

    /**
     * Devuelve la lista de permisos del usuario autenticado.
     * Este endpoint permite al frontend conocer qué acciones puede realizar el usuario.
     *
     * @param request Petición HTTP, utilizada para extraer el token de autorización.
     * @return Lista de nombres de permisos asignados al usuario autenticado.
     */
    @GetMapping("/me/permissions")
    public ResponseEntity<List<String>> getAuthenticatedUserPermissions(HttpServletRequest request) {
        // Obtener token del header Authorization
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);

        // Obtener el usuario autenticado desde el token
        Optional<User> optionalUser = authenticatedUserBl.getAuthenticatedUser(token);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }

        User user = optionalUser.get();

        // Obtener permisos del usuario desde el BL
        List<String> permissions = permissionBl.getPermissionsByUser(user);
        return ResponseEntity.ok(permissions);
    }
}
