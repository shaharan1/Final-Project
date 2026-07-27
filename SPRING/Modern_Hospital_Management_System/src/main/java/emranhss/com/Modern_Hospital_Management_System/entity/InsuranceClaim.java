package emranhss.com.Modern_Hospital_Management_System.entity;

import emranhss.com.Modern_Hospital_Management_System.enums.ClaimStatus;
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
@Table(name = "insurance_claims")
public class InsuranceClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String claimReference;

    private String claimNumber;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private String patientName;

    @Column(nullable = false)
    private Long insuranceId;

    private String insuranceCompanyName;

    private String policyNumber;

    private String invoiceNumber;

    @Column(nullable = false)
    private Double claimAmount;

    private Double approvedAmount;

    private Double paidAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimStatus claimStatus;

    private String processedBy;

    private String rejectionReason;

    private String notes;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime submissionDate;

    private LocalDateTime reviewDate;

    private LocalDateTime settlementDate;
}
