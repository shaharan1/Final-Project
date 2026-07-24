package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PharmacySaleRequest {
    private String patientType; // INPATIENT or OUTPATIENT
    private Long billingId;     // Required only if patientType is INPATIENT
    private Double discount;
    private List<PharmacySaleItemRequest> items;
}
