package emranhss.com.Modern_Hospital_Management_System.entity;

import emranhss.com.Modern_Hospital_Management_System.enums.PaymentMethod;
import emranhss.com.Modern_Hospital_Management_System.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "billing_payments")
@NoArgsConstructor
@AllArgsConstructor
public class BillingPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private BillingInvoice invoice;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentMethod paymentMethod;

    @Column(length = 100)
    private String transactionId;

    @Column(length = 100)
    private String cardLast4;

    @Column(length = 100)
    private String bankName;

    @Column(length = 100)
    private String mobileProvider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insurance_company_id")
    private Insurance insuranceCompany;

    private Double insuranceCoverage = 0.0;
    private Double selfPayAmount = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus paymentStatus = PaymentStatus.COMPLETED;

    @Column(length = 500)
    private String notes;

    @Column(length = 100)
    private String processedBy;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime paymentDate;
}
