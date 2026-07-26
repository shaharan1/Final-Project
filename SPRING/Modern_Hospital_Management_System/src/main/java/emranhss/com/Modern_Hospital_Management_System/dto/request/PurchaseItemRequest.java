package emranhss.com.Modern_Hospital_Management_System.dto.request;
import lombok.Data;
import java.time.LocalDate;
@Data
public class PurchaseItemRequest {
    private Long purchaseId;
    private Long stockId;
    private Integer quantity;
    private Double unitPrice;
    private Double discount;
    private Double vat;
    private String batchNumber;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
}
