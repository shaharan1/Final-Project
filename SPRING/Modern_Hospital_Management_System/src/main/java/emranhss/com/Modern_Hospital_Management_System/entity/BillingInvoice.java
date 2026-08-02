package emranhss.com.Modern_Hospital_Management_System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "billing_invoices")
@NoArgsConstructor
@AllArgsConstructor
public class BillingInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admitted_patient_id")
    private AdmittedPatient admittedPatient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor referringDoctor;

    @Column(nullable = false, length = 20)
    private String invoiceType = "INPATIENT"; // INPATIENT, OUTPATIENT, EMERGENCY

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BillingInvoiceItem> items = new ArrayList<>();

    private Double subtotal = 0.0;
    private Double taxRate = 0.0;
    private Double taxAmount = 0.0;
    private Double discountPercent = 0.0;
    private Double discountAmount = 0.0;
    private Double netAmount = 0.0;

    private Double totalPaid = 0.0;
    private Double dueAmount = 0.0;

    @Column(nullable = false, length = 20)
    private String paymentStatus = "UNPAID"; // UNPAID, PARTIAL, PAID, REFUNDED

    @Column(nullable = false, length = 20)
    private String invoiceStatus = "DRAFT"; // DRAFT, FINALIZED, CANCELLED

    @Column(length = 500)
    private String notes;

    @Column(length = 100)
    private String preparedBy;

    @Column(length = 100)
    private String finalizedBy;

    private LocalDateTime finalizedDate;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    public void recalculateTotals() {
        this.subtotal = items.stream()
                .mapToDouble(BillingInvoiceItem::getAmount)
                .sum();

        this.taxAmount = this.subtotal * (this.taxRate != null ? this.taxRate : 0.0) / 100.0;
        this.discountAmount = this.subtotal * (this.discountPercent != null ? this.discountPercent : 0.0) / 100.0;
        this.netAmount = this.subtotal + this.taxAmount - this.discountAmount;
        this.dueAmount = this.netAmount - (this.totalPaid != null ? this.totalPaid : 0.0);
    }
}
