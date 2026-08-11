package emranhss.com.Modern_Hospital_Management_System.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "test_admited_patients")
@NoArgsConstructor
@AllArgsConstructor
public class TestAdmitedPatient {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "test_order_id", nullable = false)
    private Tests testOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admitted_patient_id", nullable = false)
    private AdmittedPatient admittedPatient;

    @Column(nullable = false)
    private double billedAmount;

    @Column(nullable = false, length = 20)
    private String billingStatus = "PENDING"; // PENDING, BILLED

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime linkedDate;

}
