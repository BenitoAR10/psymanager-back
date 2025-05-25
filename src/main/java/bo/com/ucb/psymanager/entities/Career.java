package bo.com.ucb.psymanager.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ps_career")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Career {

    /** ID de la carrera */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "career_id")
    private Long careerId;

    /** Nombre de la carrera */
    @Column(name = "career_name", nullable = false, length = 100)
    private String careerName;

    /** Facultad asociada */
    @Column(name = "faculty", nullable = false, length = 100)
    private String faculty;

    /** Estado de la carrera */
    @Column(name = "status", nullable = false, length = 50)
    private String status;

    /** Carreras pueden tener muchos estudiantes */
    @OneToMany(mappedBy = "career", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CareerDepartment> careerDepartments = new ArrayList<>();

}