package emranhss.com.Modern_Hospital_Management_System.dto.response;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class StockHistoryResponse {
    private Long id;
    private String medicineName;
    private String batchNumber;
    private String adjustmentType;
    private Integer quantityChange;
    private Integer previousQuantity;
    private Integer newQuantity;
    private String reason;
    private String performedBy;
    private LocalDateTime adjustedAt;
}
