package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PharmacySaleResponse {
    private Long id;
    private String saleInvoiceNo;
    private String patientType;
    private Double totalAmount;
    private Double discount;
    private Double netPayable;
    private String paymentStatus;
    private LocalDateTime saleDate;
    private Long billingId; // Attached parent indoor ledger index link if present
}
