package emranhss.com.Modern_Hospital_Management_System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Inpatient meal consumption transaction log.
 * Tracks individual tray deliveries and calculates active stay billing balances.
 */
@Data
@Entity
@Table(name = "meals")
@NoArgsConstructor
@AllArgsConstructor
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double mealCost; // Snapshots master price to handle subsequent menu price inflation safely

    @Column(nullable = false)
    private LocalDateTime servedAt; // Date and precise timeframe token of physical kitchen deployment

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_master_id", nullable = false)
    private MealMaster mealMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bed_booking_id")
    private BedBooking bedBooking; // Ties consumption tracing to specific room inventory blocks

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admitted_patient_id", nullable = false)
    private AdmittedPatient admittedPatient; // Primary link mapping onto active inpatient records

    @Column(nullable = false, length = 20)
    private String billingStatus = "PENDING"; // State control to manage discharge summaries: PENDING, BILLED
}
