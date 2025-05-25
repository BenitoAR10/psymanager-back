package bo.com.ucb.psymanager.dto;

import lombok.*;

/**
 * DTO simple para representar una carrera con su ID y nombre.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerSimpleDto {
    private Long careerId;
    private String careerName;
}