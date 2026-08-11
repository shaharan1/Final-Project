package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MealResponse {
    private Long id;
    private double mealCost;
    private LocalDateTime servedAt;
    private Long mealMasterId;
    private String mealMasterName;
    private Long bedBookingId;
    private Long admittedPatientId;
    private String billingStatus;
}
