package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BillingResponse {
    private Long id;
    private Long admittedPatientId;
    private String patientName;
    private String patientCode;
    private String bedNumber;
    private Double wardCost;
    private Double mealCost;
    private Double medicineCost;
    private Double testCost;
    private Double doctorCharge;
    private Double otherCharge;
    private Double totalCost;
    private String billingStatus;
    private LocalDateTime lastUpdated;
}
