package emranhss.com.Modern_Hospital_Management_System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
  Global menu catalog managed by hospital nutritionists.
  Stores price master schemas for standard and therapeutic diets.
 */
@Data
@Entity
@Table(name = "meal_masters")
@NoArgsConstructor
@AllArgsConstructor
public class MealMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String category; //  Breakfast / Lunch / Dinner / Snack

    @Column(nullable = false, length = 50)
    private String type;     //  Set1 / Set2 / Special / Diet

    @Column(nullable = false, length = 150)
    private String name;     //  Low Sodium Diabetic Rice Meal

    @Column(columnDefinition = "TEXT")
    private String details;  // Nutritional contents/allergens description

    @Column(nullable = false)
    private Double price;    // Base catalog price per single meal unit

    private Boolean active = true;
}
