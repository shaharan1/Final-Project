package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseItemResponse {
    private Long id;
    private Long purchaseId;
    private Long stockId;
    private String medicineName; // UI-তে দেখানোর সুবিধার্থে ফ্ল্যাট ফিল্ড
    private int quantity;
    private double unitPrice;
    private double subtotal;
}
