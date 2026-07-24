package emranhss.com.Modern_Hospital_Management_System.dto.request;


import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicineStockRequest {


    private String medicineName;
    private String genericName;
    private String batchNumber;
    private Integer stockQuantity;
    private Double purchasePrice;
    private Double salePrice;
    private LocalDate expiryDate;
    private Long supplierId;



}
