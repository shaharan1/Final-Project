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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String paymentReference;

    @Column(nullable = false)
    private String invoiceNumber;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private String patientName;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    private String transactionId;

    private String cardLast4;

    private String bankName;

    private String mobileProvider;

    private Long insuranceCompanyId;

    private Double insuranceCoverage;

    private Double selfPayAmount;

    private Double discount;

    private Double VAT;

    private Double netAmount;

    private Double refundAmount;

    private String notes;

    private String processedBy;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
}
