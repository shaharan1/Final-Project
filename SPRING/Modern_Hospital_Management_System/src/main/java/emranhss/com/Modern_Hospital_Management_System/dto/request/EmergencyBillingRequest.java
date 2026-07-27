package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyBillingRequest {

    private Long emergencyPatientId;
    private Double registrationFee;
    private Double consultationFee;
    private Double bedCharge;
    private Double medicineCharge;
    private Double labCharge;
    private Double radiologyCharge;
    private Double procedureCharge;
    private Double operationCharge;
    private Double ambulanceCharge;
    private Double consumablesCharge;
    private Double doctorFee;
    private Double nursingCharge;
    private Double otherCharges;
    private Double discountPercent;
    private Double vatPercent;
    private Double insuranceCoverage;
    private Double advancePaid;
    private String insuranceProvider;
    private String insurancePolicyNumber;
    private Boolean isInsuranceClaimed;
    private String notes;
}
