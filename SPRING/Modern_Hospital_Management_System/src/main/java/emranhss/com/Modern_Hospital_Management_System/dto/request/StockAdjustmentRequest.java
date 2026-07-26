package emranhss.com.Modern_Hospital_Management_System.dto.request;
import lombok.Data;
@Data
public class StockAdjustmentRequest {
    private Long medicineStockId;
    private String adjustmentType;
    private Integer quantityChange;
    private String reason;
    private String performedBy;
}
