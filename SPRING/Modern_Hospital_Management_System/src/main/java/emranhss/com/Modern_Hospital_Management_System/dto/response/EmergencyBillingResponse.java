package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyBillingResponse {

    private Long id;
    private Long emergencyPatientId;
    private String emergencyNumber;
    private String billNumber;
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
    private Double subtotal;
    private Double discountPercent;
    private Double discountAmount;
    private Double vatPercent;
    private Double vatAmount;
    private Double insuranceCoverage;
    private Double advancePaid;
    private Double dueAmount;
    private Double grandTotal;
    private String paymentStatus;
    private String insuranceProvider;
    private String insurancePolicyNumber;
    private Boolean isInsuranceClaimed;
    private LocalDateTime paidAt;
    private String status;
    private String notes;
}
