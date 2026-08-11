package emranhss.com.Modern_Hospital_Management_System.dto.response;
import lombok.Data;
import java.time.LocalDate;
@Data
public class PurchaseItemResponse {
    private Long id;
    private Long purchaseId;
    private Long stockId;
    private String medicineName;
    private String batchNumber;
    private Integer quantity;
    private Double unitPrice;
    private Double discount;
    private Double vat;
    private Double subtotal;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
}
