package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmbulanceTripRequest {

    private Long ambulanceId;
    private Long emergencyPatientId;
    private String tripType;
    private String pickupLocation;
    private String dropoffLocation;
    private String notes;
}
