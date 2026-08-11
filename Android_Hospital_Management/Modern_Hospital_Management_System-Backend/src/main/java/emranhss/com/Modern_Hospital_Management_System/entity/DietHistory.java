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
@Table(name = "diet_history")
@NoArgsConstructor
@AllArgsConstructor
public class DietHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_assignment_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private DietAssignment dietAssignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_plan_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private DietPlan dietPlan;

    @Column(nullable = false)
    private String actionType; // ASSIGNED, UPDATED, CANCELLED, DOCTOR_RECOMMENDATION, DIETICIAN_RECOMMENDATION, MEAL_CHANGED

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String previousValue;

    @Column(columnDefinition = "TEXT")
    private String newValue;

    private String performedBy;

    private String userRole;

    private Double weightKg;

    private Double bmi;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
