package emranhss.com.Modern_Hospital_Management_System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "billings")
@NoArgsConstructor
@AllArgsConstructor
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admitted_patient_id", nullable = false, unique = true)
    private AdmittedPatient admittedPatient;

    private Double wardCost = 0.0;
    private Double mealCost = 0.0;
    private Double medicineCost = 0.0;
    private Double testCost = 0.0;
    private Double doctorCharge = 0.0;
    private Double otherCharge = 0.0;
    private Double totalCost = 0.0;

    @Column(nullable = false, length = 20)
    private String billingStatus = "OPEN"; // OPEN, CLOSED

    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    // Helper method to automatically recalculate the grand total sum
    public void calculateTotalCost() {
        this.totalCost = (this.wardCost != null ? this.wardCost : 0.0)
                + (this.mealCost != null ? this.mealCost : 0.0)
                + (this.medicineCost != null ? this.medicineCost : 0.0)
                + (this.testCost != null ? this.testCost : 0.0)
                + (this.doctorCharge != null ? this.doctorCharge : 0.0)
                + (this.otherCharge != null ? this.otherCharge : 0.0);
    }
}
