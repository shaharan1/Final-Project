package emranhss.com.Modern_Hospital_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "pharmacy_sales")
@NoArgsConstructor
@AllArgsConstructor
public class PharmacySale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String saleInvoiceNo; // Generated automatically (e.g., PHM-INV-XXXX)

    private String patientType; // INPATIENT, OUTPATIENT
    private Double totalAmount = 0.0;
    private Double discount = 0.0;
    private Double netPayable = 0.0;
    private String paymentStatus; // PAID, PENDING_BILLING

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime saleDate;

    // Fixed Link: Connects back to your existing Billing entity for Inpatients
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_id", nullable = true)
    @JsonIgnoreProperties("admittedPatient")
    private Billing billing;

    @OneToMany(mappedBy = "pharmacySale", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("pharmacySale")
    private List<PharmacySaleItem> saleItems = new ArrayList<>();
}
