package emranhss.com.Modern_Hospital_Management_System.dto.request;
import lombok.Data;
import java.util.List;
@Data
public class PurchaseRequest {
    private Long supplierId;
    private Double vat;
    private Double discount;
    private Double paidAmount;
    private String paymentMethod;
    private String notes;
    private List<PurchaseItemRequest> items;
}
