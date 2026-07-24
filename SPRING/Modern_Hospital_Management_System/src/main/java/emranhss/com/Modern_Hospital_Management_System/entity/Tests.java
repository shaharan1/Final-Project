package emranhss.com.Modern_Hospital_Management_System.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tests")
@NoArgsConstructor
@AllArgsConstructor
public class Tests {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_master_id", nullable = false)
    private TestMaster testMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor prescribedBy;

    @Column(nullable = false)
    private String orderStatus = "PENDING"; // PENDING, SAMPLE_COLLECTED, COMPLETED

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime orderedDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;
}
