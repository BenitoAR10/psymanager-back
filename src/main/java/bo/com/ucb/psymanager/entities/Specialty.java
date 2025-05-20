package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name = "ps_specialty")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specialty_id")
    private Integer specialtyId;

    @Column(name = "specialty_name", length = 100, nullable = false)
    private String specialtyName;

    @Column(name = "description")
    private String description;

}