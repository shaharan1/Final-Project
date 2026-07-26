package emranhss.com.Modern_Hospital_Management_System.dto.response;
import lombok.Data;
@Data
public class PharmacySaleItemResponse {
    private Long id;
    private Long medicineStockId;
    private String medicineName;
    private String batchNumber;
    private Integer quantity;
    private Double unitPrice;
    private Double discount;
    private Double subtotal;
}
