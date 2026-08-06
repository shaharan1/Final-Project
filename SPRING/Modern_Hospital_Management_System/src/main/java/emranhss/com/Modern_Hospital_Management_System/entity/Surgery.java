package emranhss.com.Modern_Hospital_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "surgeries")
@NoArgsConstructor
@AllArgsConstructor
public class Surgery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String surgeryNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admitted_patient_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "patient", "primaryDoctor"})
    private AdmittedPatient admittedPatient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surgeon_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "user", "slots", "reports"})
    private Doctor surgeon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assistant_surgeon_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "user", "slots", "reports"})
    private Doctor assistantSurgeon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anesthesiologist_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "user", "slots", "reports"})
    private Doctor anesthesiologist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "doctors"})
    private DoctorDepartment department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private SurgeryCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surgery_master_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "category"})
    private SurgeryMaster surgeryMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_theatre_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private OperationTheatre operationTheatre;

    @Column(nullable = false)
    private LocalDate surgeryDate;

    private LocalTime startTime;
    private LocalTime endTime;
    private Integer estimatedDurationMin = 60;

    @Column(length = 20)
    private String priority = "ELECTIVE"; // EMERGENCY, URGENT, ELECTIVE

    @Column(length = 100)
    private String anesthesiaType; // GENERAL, SPINAL, LOCAL, REGIONAL, EPIDURAL

    @Column(columnDefinition = "TEXT")
    private String clinicalNotes;

    @Column(columnDefinition = "TEXT")
    private String preOperativeDiagnosis;

    @Column(columnDefinition = "TEXT")
    private String postOperativeDiagnosis;

    @Column(length = 20)
    private String status = "SCHEDULED"; // SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED, POSTPONED

    @Column(length = 300)
    private String cancellationReason;

    // ======================= CHARGE BREAKDOWN =======================

    private Double surgeryCharge = 0.0;
    private Double otCharge = 0.0;
    private Double surgeonFee = 0.0;
    private Double assistantSurgeonFee = 0.0;
    private Double anesthesiaFee = 0.0;
    private Double nursingCharge = 0.0;
    private Double equipmentCharge = 0.0;
    private Double consumableCharge = 0.0;
    private Double icuCharge = 0.0;
    private Double wardCabinCharge = 0.0;
    private Double medicineCharge = 0.0;
    private Double laboratoryCharge = 0.0;
    private Double radiologyCharge = 0.0;

    private Double discountPercent = 0.0;
    private Double discountAmount = 0.0;
    private Double vatRate = 0.0;
    private Double vatAmount = 0.0;
    private Double insuranceCoverage = 0.0;
    private Double advancePaid = 0.0;
    private Double finalPayable = 0.0;

    // ======================= BILLING INTEGRATION =======================

    private Long billingInvoiceId;
    @Column(length = 50)
    private String billingInvoiceNumber;

    @Column(length = 20)
    private String billingStatus = "NOT_BILLED"; // NOT_BILLED, DRAFT, FINALIZED, PAID

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime lastUpdated;
}
