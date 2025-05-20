package bo.com.ucb.psymanager.dto;


import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO para actualizar y completar el perfil profesional del terapeuta.
 * Incluye datos personales, especialidades y periodo de servicio.
 */
@Data
public class TherapistProfileUpdateDto {

    /** Número de CI del terapeuta */
    private String ciNumber;

    /** Complemento del CI (opcional) */
    private String ciComplement;

    /** Extensión del CI (por ejemplo: LP, CB, SCZ) */
    private String ciExtension;

    /** Teléfono de contacto del terapeuta */
    private String phoneNumber;

    /** Dirección del terapeuta (opcional) */
    private String address;

    /** Lista de IDs de especialidades seleccionadas */
    private List<Integer> specialtyIds;

    /** Cargo o rol actual dentro del servicio (ej: Pasante, Encargado) */
    private String position;

    /** Fecha de inicio del periodo de servicio actual */
    private LocalDate startDate;

    /** Fecha de finalización del periodo (puede ser null si sigue en curso) */
    private LocalDate endDate;
}