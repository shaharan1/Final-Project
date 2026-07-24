package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MealRequest {
    private double mealCost;
    private LocalDateTime servedAt;
    private Long mealMasterId;
    private Long bedBookingId;
    private Long admittedPatientId;
    private String billingStatus;
}
