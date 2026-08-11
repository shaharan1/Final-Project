package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingPaymentRequest {

    private Long invoiceId;
    private Double amount;
    private String paymentMethod; // CASH, CARD, BANK_TRANSFER, MOBILE_BANKING, INSURANCE, SPLIT
    private String transactionId;
    private String cardLast4;
    private String bankName;
    private String mobileProvider;
    private Long insuranceCompanyId;
    private Double insuranceCoverage;
    private Double selfPayAmount;
    private String notes;
    private String processedBy;
}
