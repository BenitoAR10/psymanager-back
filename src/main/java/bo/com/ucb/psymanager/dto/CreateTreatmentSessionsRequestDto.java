package bo.com.ucb.psymanager.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Permite al terapeuta añadir una sesión puntual dentro de un plan existente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTreatmentSessionsRequestDto {

    private List<Long> slotIds;
}