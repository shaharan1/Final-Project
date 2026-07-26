package emranhss.com.Modern_Hospital_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "patient_diet_alerts")
@NoArgsConstructor
@AllArgsConstructor
public class PatientDietAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admitted_patient_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private AdmittedPatient admittedPatient;

    @Column(nullable = false)
    private String alertType; // DIABETIC, LOW_SODIUM, ALLERGY, NPO, FASTING, CRITICAL, FOOD_ALLERGY, KITCHEN_ALERT, LATE_DELIVERY

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String severity; // LOW, MEDIUM, HIGH, CRITICAL

    @Column(nullable = false)
    private String status; // ACTIVE, ACKNOWLEDGED, RESOLVED

    private String allergenName; // For FOOD_ALLERGY type

    @Column(columnDefinition = "TEXT")
    private String specialInstructions;

    private String createdBy;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime resolvedAt;
}
