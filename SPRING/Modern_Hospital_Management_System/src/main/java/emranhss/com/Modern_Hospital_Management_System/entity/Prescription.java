package emranhss.com.Modern_Hospital_Management_System.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "prescriptions")
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String chiefComplaints;

    @Column(columnDefinition = "TEXT")
    private String symptoms;

    // Patient Vitals
    private String bloodPressure;
    private String pulseRate;
    private String bodyTemperature;
    private String weight;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private LocalDate nextFollowUpDate;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrescriptionItem> prescriptionItems = new ArrayList<>();

    @Column(unique = true)
    private String prescriptionNumber;


}
