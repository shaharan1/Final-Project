package emranhss.com.Modern_Hospital_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "kitchen_orders")
@NoArgsConstructor
@AllArgsConstructor
public class KitchenOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber; // e.g., KO-20260727-001

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
    @JoinColumn(name = "diet_assignment_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private DietAssignment dietAssignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Ward ward;

    private String bedNumber;

    @Column(nullable = false)
    private String mealTime; // BREAKFAST, MORNING_SNACKS, LUNCH, EVENING_SNACKS, DINNER, NIGHT_DIET

    @Column(nullable = false)
    private String mealType; // The actual meal content description

    @Column(nullable = false)
    private String dietType; // Diet type from the plan

    @Column(nullable = false)
    private String priority; // NORMAL, HIGH, URGENT

    @Column(nullable = false)
    private String status; // PENDING, PREPARING, COOKING, READY, DELIVERED, CANCELLED

    @Column(columnDefinition = "TEXT")
    private String kitchenNotes;

    private String preparedBy;
    private String deliveredBy;

    private LocalDateTime preparingAt;
    private LocalDateTime cookingAt;
    private LocalDateTime readyAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;

    private Boolean specialDiet = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
