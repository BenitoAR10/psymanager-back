package bo.com.ucb.psymanager.controllers;

import bo.com.ucb.psymanager.bl.AuthenticatedUserBl;
import bo.com.ucb.psymanager.bl.PermissionBl;
import bo.com.ucb.psymanager.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
     * @param email Email del usuario autenticado (extraído del token JWT)
     * @return Lista de nombres de permisos asignados al usuario autenticado.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me/permissions")
    public ResponseEntity<List<String>> getAuthenticatedUserPermissions(@AuthenticationPrincipal String email) {
        User user = authenticatedUserBl.getUserByEmail(email);
        List<String> permissions = permissionBl.getPermissionsByUser(user);
        return ResponseEntity.ok(permissions);
    }

}
