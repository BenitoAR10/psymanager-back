package bo.com.ucb.psymanager.dto;

import lombok.*;

/**
 * DTO para reportar errores de validación por campo.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldErrorResponse {
    /**
     * Nombre del campo que causó el error (por ejemplo: "ciNumber", "phoneNumber").
     */
    private String field;

    /**
     * Mensaje de error legible para el usuario.
     */
    private String message;
}