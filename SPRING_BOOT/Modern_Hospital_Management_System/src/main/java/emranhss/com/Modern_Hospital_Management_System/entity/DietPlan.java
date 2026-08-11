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
@Table(name = "diet_plans")
@NoArgsConstructor
@AllArgsConstructor
public class DietPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String dietType; // Regular, Diabetic, LowSalt, LowFat, Cardiac, HighProtein, Liquid, Soft, Renal, Pediatric, Pregnancy, PostSurgery, Special

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_dietician_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Dietician createdByDietician;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_doctor_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Doctor approvedByDoctor;

    // Breakfast
    @Column(columnDefinition = "TEXT")
    private String breakfast;
    private String breakfastTime;

    // Morning Snacks
    @Column(columnDefinition = "TEXT")
    private String morningSnacks;
    private String morningSnacksTime;

    // Lunch
    @Column(columnDefinition = "TEXT")
    private String lunch;
    private String lunchTime;

    // Evening Snacks
    @Column(columnDefinition = "TEXT")
    private String eveningSnacks;
    private String eveningSnacksTime;

    // Dinner
    @Column(columnDefinition = "TEXT")
    private String dinner;
    private String dinnerTime;

    // Night Diet
    @Column(columnDefinition = "TEXT")
    private String nightDiet;
    private String nightDietTime;

    // Nutrition Information
    private Double totalCalories;
    private Double protein;
    private Double carbohydrate;
    private Double fat;
    private Double fiber;
    private Double sodium;
    private Double potassium;
    private Double waterIntakeMl;

    @Column(columnDefinition = "TEXT")
    private String vitaminRecommendation;

    @Column(columnDefinition = "TEXT")
    private String doctorRecommendation;

    @Column(columnDefinition = "TEXT")
    private String dieticianNotes;

    private Double pricePerDay = 0.0;

    private Boolean active = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
