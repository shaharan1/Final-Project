package emranhss.com.Modern_Hospital_Management_System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "meal_schedules")
@NoArgsConstructor
@AllArgsConstructor
public class MealSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String mealName; // Breakfast, Morning Snacks, Lunch, Evening Snacks, Dinner, Night Diet

    @Column(nullable = false)
    private LocalTime servingTime;

    @Column(nullable = false)
    private LocalTime preparationStartTime;

    private LocalTime preparationEndTime;

    @Column(nullable = false)
    private String status; // ACTIVE, INACTIVE

    @Column(nullable = false)
    private Integer totalOrdersToday;

    private Integer completedOrders;
    private Integer pendingOrders;
    private Integer cancelledOrders;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "is_current_meal")
    private Boolean currentMeal = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
