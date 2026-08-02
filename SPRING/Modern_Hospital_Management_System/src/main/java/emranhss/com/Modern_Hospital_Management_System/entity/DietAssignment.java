package emranhss.com.Modern_Hospital_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "diet_assignments")
@NoArgsConstructor
@AllArgsConstructor
public class DietAssignment {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_plan_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private DietPlan dietPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by_doctor_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Doctor assignedByDoctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dietician_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Dietician dietician;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    private String status; // ACTIVE, COMPLETED, CANCELLED, ON_HOLD

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String specialInstructions;

    private Double targetCalories;
    private Double targetWeight;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Transient
    private String wardName;

    @Transient
    private String bedNumber;

    @Transient
    private Double totalAmount;
}
