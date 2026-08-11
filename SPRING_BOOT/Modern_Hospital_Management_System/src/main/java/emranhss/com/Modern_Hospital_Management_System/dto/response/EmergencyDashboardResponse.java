package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyDashboardResponse {

    private Long emergencyPatientsToday;
    private Long criticalPatients;
    private Long waitingPatients;
    private Long patientsUnderTreatment;
    private Long admittedFromEmergency;
    private Long ambulancesActive;
    private Long doctorsOnDuty;
    private Long nursesOnDuty;
    private Long availableEmergencyBeds;
    private Long icuBedsAvailable;
    private Double todaysEmergencyRevenue;
    private Double averageWaitingTime;
}
