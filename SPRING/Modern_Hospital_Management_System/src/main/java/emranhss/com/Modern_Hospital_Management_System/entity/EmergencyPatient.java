package emranhss.com.Modern_Hospital_Management_System.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "emergency_patients")
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyPatient {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private String severityLevel; // CRITICAL, STABLE, TRIAGE_A

    @Column(columnDefinition = "TEXT")
    private String chiefComplaint;

    private String broughtBy;
    private String contactNumber;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime arrivalTime;

    private String status; // TREATING, ADMITTED, DISCHARGED
}
