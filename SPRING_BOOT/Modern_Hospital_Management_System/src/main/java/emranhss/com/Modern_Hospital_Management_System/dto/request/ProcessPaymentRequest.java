package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentRequest {

    private String invoiceNumber;
    private Long patientId;
    private String patientName;
    private Double amount;
    private String paymentMethod;
    private String transactionId;
    private String cardLast4;
    private String bankName;
    private String mobileProvider;
    private Long insuranceCompanyId;
    private Double insuranceCoverage;
    private Double selfPayAmount;
    private Double discount;
    private Double VAT;
    private String notes;
    private String processedBy;
}
