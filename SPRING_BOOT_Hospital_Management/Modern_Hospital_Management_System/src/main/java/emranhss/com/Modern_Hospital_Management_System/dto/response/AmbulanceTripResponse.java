package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmbulanceTripResponse {

    private Long id;
    private Long ambulanceId;
    private String ambulanceNumber;
    private Long emergencyPatientId;
    private String emergencyNumber;
    private String tripType;
    private String pickupLocation;
    private String dropoffLocation;
    private LocalDateTime dispatchTime;
    private LocalDateTime arrivalTime;
    private LocalDateTime completionTime;
    private Double distanceTravelled;
    private Double responseTimeMinutes;
    private String status;
    private String notes;
}
