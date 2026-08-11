package emranhss.com.Modern_Hospital_Management_System.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "test_masters")
@NoArgsConstructor
@AllArgsConstructor
public class TestMaster {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String testCode; // CBC, MRI-LUMBAR, LIPID-01

    @Column(nullable = false, length = 150)
    private String testName; //  Complete Blood Count, Lipid Profile

    @Column(nullable = false)
    private double standardPrice;

    private String normalRange; //  4.5 - 11.0 x10^3/uL

    private Boolean active = true;


}
