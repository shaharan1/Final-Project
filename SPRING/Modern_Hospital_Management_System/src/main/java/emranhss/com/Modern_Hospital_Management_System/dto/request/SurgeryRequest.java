package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurgeryRequest {

    private Long patientId;
    private Long admittedPatientId;
    private Long surgeonId;
    private Long assistantSurgeonId;
    private Long anesthesiologistId;
    private Long departmentId;
    private Long categoryId;
    private Long surgeryMasterId;
    private Long operationTheatreId;

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

    private Double discountPercent;
    private Double vatRate;
    private Double insuranceCoverage;
    private Double advancePaid;

    private String cancellationReason;
}
