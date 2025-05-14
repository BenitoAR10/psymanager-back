package bo.com.ucb.psymanager.dto;

import bo.com.ucb.psymanager.entities.SessionState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSessionStateRequestDto {
    private SessionState newState;
}
