package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDiscountRequest {
    private Long doctorId;
    private Double fixedDiscount;
    private Double percentageDiscount;
    private Double departmentDiscount;
    private Double specialPromoDiscount;
    private Boolean active;
    private String notes;
}
