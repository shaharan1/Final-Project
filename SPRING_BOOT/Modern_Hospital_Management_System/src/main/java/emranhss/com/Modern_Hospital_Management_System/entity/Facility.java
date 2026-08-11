package emranhss.com.Modern_Hospital_Management_System.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "facilities")
@NoArgsConstructor
@AllArgsConstructor
public class Facility {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name; // e.g., Centralized Oxygen, Ventilator, AC, Monitor

    private Double standardCharge; // Extra daily utility fee added to base billing

    private Boolean active = true;
}
