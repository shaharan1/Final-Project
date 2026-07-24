package emranhss.com.Modern_Hospital_Management_System.dto.response;


import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicineStockResponse {

    private Long id;
    private String medicineName;
    private String genericName;
    private String batchNumber;
    private Integer stockQuantity;
    private Double purchasePrice;
    private Double salePrice;
    private LocalDate expiryDate;
    private Long supplierId;
    private String supplierName;
    private String inventoryStatus; //  AVAILABLE, LOW_STOCK, EXPIRED


}
