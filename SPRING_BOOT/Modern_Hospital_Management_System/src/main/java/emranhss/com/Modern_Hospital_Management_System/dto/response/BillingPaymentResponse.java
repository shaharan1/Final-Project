package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BillingPaymentResponse {

    private Long id;
    private Long invoiceId;
    private String invoiceNumber;
    private String patientName;
    private Double amount;
    private String paymentMethod;
    private String transactionId;
    private String paymentStatus;
    private String notes;
    private String processedBy;
    private LocalDateTime paymentDate;

    @Data
    public static class PaymentListResponse {
        private List<BillingPaymentResponse> payments;
        private Double totalAmount;
        private long count;
    }
}
