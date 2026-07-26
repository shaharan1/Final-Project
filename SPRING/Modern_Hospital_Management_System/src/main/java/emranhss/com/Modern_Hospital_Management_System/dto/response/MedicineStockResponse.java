package emranhss.com.Modern_Hospital_Management_System.dto.response;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class MedicineStockResponse {
    private Long id;
    private String medicineName;
    private String genericName;
    private String strength;
    private String dosageForm;
    private String batchNumber;
    private Integer stockQuantity;
    private Integer reservedQuantity;
    private Integer damagedQuantity;
    private Integer availableQuantity;
    private Double purchasePrice;
    private Double salePrice;
    private Double vat;
    private Integer minimumStockLevel;
    private Integer reorderLevel;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    private String barcode;
    private Long supplierId;
    private String supplierName;
    private Boolean active;
    private Boolean expired;
    private Boolean expiringSoon;
    private Boolean lowStock;
    private String inventoryStatus;
    private LocalDateTime createdDate;
}
