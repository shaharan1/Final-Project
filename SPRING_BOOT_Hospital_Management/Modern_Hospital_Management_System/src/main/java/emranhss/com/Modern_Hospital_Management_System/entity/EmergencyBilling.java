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
@Table(name = "emergency_billing")
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyBilling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emergency_patient_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private EmergencyPatient emergencyPatient;

    @Column(unique = true, nullable = false)
    private String billNumber;

    private Double registrationFee = 0.0;

    private Double consultationFee = 0.0;

    private Double bedCharge = 0.0;

    private Double medicineCharge = 0.0;

    private Double labCharge = 0.0;

    private Double radiologyCharge = 0.0;

    private Double procedureCharge = 0.0;

    private Double operationCharge = 0.0;

    private Double ambulanceCharge = 0.0;

    private Double consumablesCharge = 0.0;

    private Double doctorFee = 0.0;

    private Double nursingCharge = 0.0;

    private Double otherCharges = 0.0;

    private Double subtotal = 0.0;

    private Double discountPercent = 0.0;

    private Double discountAmount = 0.0;

    private Double vatPercent = 18.0;

    private Double vatAmount = 0.0;

    private Double insuranceCoverage = 0.0;

    private Double advancePaid = 0.0;

    private Double dueAmount = 0.0;

    private Double grandTotal = 0.0;

    private String paymentStatus;

    private String insuranceProvider;

    private String insurancePolicyNumber;

    private Boolean isInsuranceClaimed = false;

    private LocalDateTime paidAt;

    @Column(nullable = false)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
