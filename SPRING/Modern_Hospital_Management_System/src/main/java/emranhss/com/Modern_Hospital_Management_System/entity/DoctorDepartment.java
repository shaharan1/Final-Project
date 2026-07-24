package emranhss.com.Modern_Hospital_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Entity
@Table(name = "doctor_departments")
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String departmentName;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Relational Link: One department contains a list of many distinct Doctors
    @OneToMany(mappedBy = "doctorDepartment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("doctorDepartment")
    private List<Doctor> doctors;
}
