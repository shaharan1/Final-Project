package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurgeryResponse {

    private Long id;
    private String surgeryNumber;

    private Long patientId;
    private String patientName;
    private String patientCode;
    private String patientPhone;

    private Long admittedPatientId;
    private String admissionStatus;
    private Long bedId;
    private Long wardId;
    private String wardName;
    private String bedNumber;

    private Long surgeonId;
    private String surgeonName;
    private Long assistantSurgeonId;
    private String assistantSurgeonName;
    private Long anesthesiologistId;
    private String anesthesiologistName;

    private Long departmentId;
    private String departmentName;
    private Long categoryId;
    private String categoryName;
    private Long surgeryMasterId;
    private String surgeryName;
    private String surgeryCode;
    private Long operationTheatreId;
    private String operationTheatreName;
    private String otCode;

    private LocalDate surgeryDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer estimatedDurationMin;
    private String priority;
    private String anesthesiaType;
    private String clinicalNotes;
    private String preOperativeDiagnosis;
    private String postOperativeDiagnosis;
    private String status;
    private String cancellationReason;

    private Double surgeryCharge;
    private Double otCharge;
    private Double surgeonFee;
    private Double assistantSurgeonFee;
    private Double anesthesiaFee;
    private Double nursingCharge;
    private Double equipmentCharge;
    private Double consumableCharge;
    private Double icuCharge;
    private Double wardCabinCharge;
    private Double medicineCharge;
    private Double laboratoryCharge;
    private Double radiologyCharge;

    private Double subtotal;
    private Double discountPercent;
    private Double discountAmount;
    private Double vatRate;
    private Double vatAmount;
    private Double insuranceCoverage;
    private Double advancePaid;
    private Double totalAmount;
    private Double finalPayable;

    private Long billingInvoiceId;
    private String billingInvoiceNumber;
    private String billingStatus;

    private LocalDateTime createdDate;
    private LocalDateTime lastUpdated;
}
