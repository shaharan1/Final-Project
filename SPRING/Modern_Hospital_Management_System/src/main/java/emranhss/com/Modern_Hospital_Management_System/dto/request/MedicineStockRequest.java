package emranhss.com.Modern_Hospital_Management_System.dto.request;
import lombok.Data;
import java.time.LocalDate;
@Data
public class MedicineStockRequest {
    private String medicineName;
    private String genericName;
    private String strength;
    private String dosageForm;
    private String batchNumber;
    private Integer stockQuantity;
    private Double purchasePrice;
    private Double salePrice;
    private Double vat;
    private Integer minimumStockLevel;
    private Integer reorderLevel;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    private String barcode;
    private Long supplierId;
}
