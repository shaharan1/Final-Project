package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponse {
    private Long id;
    private Long supplierId;
    private String supplierName; // UI-তে সহজে দেখানোর জন্য ফ্ল্যাট ফিল্ড
    private String invoiceNo;
    private Date purchaseDate;
    private double totalAmount;
    private Date createdAt;
}
