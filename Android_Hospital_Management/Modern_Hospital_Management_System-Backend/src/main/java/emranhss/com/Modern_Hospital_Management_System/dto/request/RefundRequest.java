package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundRequest {

    private Long paymentId;
    private String invoiceNumber;
    private Long patientId;
    private String patientName;
    private Double refundAmount;
    private String refundReason;
    private String refundType;
    private String processedBy;
    private String notes;
}
