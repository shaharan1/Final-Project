package emranhss.com.Modern_Hospital_Management_System.dto.response;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class PurchaseResponse {
    private Long id;
    private Long supplierId;
    private String supplierName;
    private String invoiceNo;
    private LocalDateTime purchaseDate;
    private Double totalAmount;
    private Double vat;
    private Double discount;
    private Double netAmount;
    private Double paidAmount;
    private Double dueAmount;
    private String status;
    private String paymentStatus;
    private String paymentMethod;
    private String notes;
    private List<PurchaseItemResponse> items;
}
