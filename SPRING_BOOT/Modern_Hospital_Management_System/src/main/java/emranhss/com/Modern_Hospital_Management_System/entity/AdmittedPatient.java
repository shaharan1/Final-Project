package emranhss.com.Modern_Hospital_Management_System.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "admitted_patients")
@NoArgsConstructor
@AllArgsConstructor
public class AdmittedPatient {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor primaryDoctor;

    @Column(columnDefinition = "TEXT")
    private String initialDiagnosis;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime admissionDate;

    private LocalDateTime dischargeDate; // Populated only at final checkout

    @Column(nullable = false)
    private String admissionStatus; // ADMITTED, DISCHARGED, TRANSFERRED
}
