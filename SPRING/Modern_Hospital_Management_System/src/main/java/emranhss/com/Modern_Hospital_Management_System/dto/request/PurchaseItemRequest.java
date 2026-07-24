package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseItemRequest {
    private Long purchaseId;
    private Long stockId;
    private int quantity;
    private double unitPrice;
}
