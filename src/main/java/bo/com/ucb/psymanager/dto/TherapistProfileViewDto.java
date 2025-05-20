package bo.com.ucb.psymanager.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TherapistProfileViewDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String ciNumber;
    private String ciComplement;
    private String ciExtension;
    private List<SpecialtyDto> specialties;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SpecialtyDto {
        private Integer specialtyId;
        private String specialtyName;
    }
}