package emranhss.com.Modern_Hospital_Management_System.entity;

import emranhss.com.Modern_Hospital_Management_System.enums.RefundStatus;
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
@Table(name = "refunds")
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String refundReference;

    private Long paymentId;

    @Column(nullable = false)
    private String invoiceNumber;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private String patientName;

    @Column(nullable = false)
    private Double originalAmount;

    @Column(nullable = false)
    private Double refundAmount;

    private String refundReason;

    private String refundType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundStatus refundStatus;

    private String approvedBy;

    private String rejectionReason;

    private String processedBy;

    private String notes;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime processedDate;
}
