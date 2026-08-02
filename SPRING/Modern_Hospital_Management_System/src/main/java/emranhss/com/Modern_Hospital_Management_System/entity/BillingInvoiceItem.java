package emranhss.com.Modern_Hospital_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "billing_invoice_items")
@NoArgsConstructor
@AllArgsConstructor
public class BillingInvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    @JsonIgnore
    private BillingInvoice invoice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "charge_category_id", nullable = false)
    private ChargeCategory chargeCategory;

    @Column(nullable = false, length = 50)
    private String categoryCode;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(nullable = false)
    private Double unitPrice = 0.0;

    private Double discountPercent = 0.0;
    private Double discountAmount = 0.0;
    private Double amount = 0.0;

    @Column(length = 100)
    private String sourceModule; // ADMISSION, DOCTOR_CHARGE, PHARMACY, LAB, DIET, MANUAL

    private Long sourceId; // ID from the source module

    @Column(length = 50)
    private String itemStatus = "ACTIVE"; // ACTIVE, CANCELLED

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @PrePersist
    @PreUpdate
    public void calculateAmount() {
        double base = (quantity != null ? quantity : 1) * (unitPrice != null ? unitPrice : 0.0);
        this.discountAmount = base * (discountPercent != null ? discountPercent : 0.0) / 100.0;
        this.amount = base - this.discountAmount;
    }
}
