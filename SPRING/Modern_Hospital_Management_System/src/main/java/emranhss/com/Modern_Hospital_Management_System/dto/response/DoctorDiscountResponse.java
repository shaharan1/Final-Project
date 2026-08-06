package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDiscountResponse {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private Double fixedDiscount;
    private Double percentageDiscount;
    private Double departmentDiscount;
    private Double specialPromoDiscount;
    private Double effectiveDiscountPercent;
    private Boolean active;
    private String notes;
}
