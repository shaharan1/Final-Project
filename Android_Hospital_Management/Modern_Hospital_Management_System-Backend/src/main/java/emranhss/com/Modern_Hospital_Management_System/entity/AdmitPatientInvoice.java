package emranhss.com.Modern_Hospital_Management_System.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "admit_patient_invoices")
@NoArgsConstructor
@AllArgsConstructor
public class AdmitPatientInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String invoiceNumber; // Auto-generated string parameter (e.g., DINV-2026-XXXX)

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admitted_patient_id", nullable = false)
    private AdmittedPatient admittedPatient;

    private double bedCharges;
    private double doctorCharges;
    private double testCharges;
    private double medicineCharges;
    private double mealCharges;
    private double otherCharges;

    private double totalAmount;
    private double tax;
    private double discount;
    private double netPayable;
    private double paidAmount;
    private double dueAmount;

    @Column(nullable = false)
    private String paymentStatus; // PAID, UNPAID, PARTIAL

    private String generatedBy;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;
}
