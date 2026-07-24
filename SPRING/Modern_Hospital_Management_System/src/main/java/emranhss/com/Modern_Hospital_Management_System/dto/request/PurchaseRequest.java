package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequest {
    private Long supplierId;
    private Date purchaseDate;
}
