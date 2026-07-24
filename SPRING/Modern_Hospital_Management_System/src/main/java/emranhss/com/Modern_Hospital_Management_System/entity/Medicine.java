package emranhss.com.Modern_Hospital_Management_System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "medicines")
@NoArgsConstructor
@AllArgsConstructor
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String medicineName;

    private String dosage;

    @ManyToOne
    @JoinColumn(name = "generic_id")
    private Generic generic;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "prescription_id", nullable = false )
//    private Prescription prescription;
}