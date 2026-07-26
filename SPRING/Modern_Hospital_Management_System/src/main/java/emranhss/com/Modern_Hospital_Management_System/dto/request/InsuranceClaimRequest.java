package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceClaimRequest {

    private Long insuranceId;
    private String policyNumber;
    private Long patientId;
    private String patientName;
    private String invoiceNumber;
    private Double claimAmount;
    private String notes;
    private String processedBy;
}
