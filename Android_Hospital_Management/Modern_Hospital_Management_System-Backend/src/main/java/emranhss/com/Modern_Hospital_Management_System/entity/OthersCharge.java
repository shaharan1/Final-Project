package emranhss.com.Modern_Hospital_Management_System.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "others_charges")
@NoArgsConstructor
@AllArgsConstructor
public class OthersCharge {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String category; // e.g., AMBULANCE, CONSUMABLES, PROCEDURES, VENTILATOR

    @Column(nullable = false)
    private String description; // e.g., "Disposable Admission Kit", "Oxygen Mask Set"

    @Column(nullable = false)
    private double unitPrice; // Base price of a single item unit

    @Column(nullable = false)
    private Integer quantity = 1; // Number of items or usage frequencies

    @Column(nullable = false)
    private double amount; // Dynamically computed auto value (quantity * unitPrice)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bed_booking_id")
    private BedBooking bedBooking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admitted_patient_id", nullable = false)
    private AdmittedPatient admittedPatient;

    @Column(nullable = false, length = 20)
    private String billingStatus = "PENDING"; // PENDING, BILLED

    private String enteredBy; // Tracks accountability (e.g., "nurse_rahima")

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    // Lifecycle helper to enforce accurate calculation before saving to the database
    @PrePersist
    @PreUpdate
    public void calculateTotalAmount() {
        if (this.quantity == null || this.quantity <= 0) {
            this.quantity = 1;
        }
        this.amount = this.unitPrice * this.quantity;
    }
}
