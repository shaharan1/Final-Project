package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmbulanceRequest {

    private String ambulanceNumber;
    private String vehicleType;
    private String vehiclePlate;
    private String driverName;
    private String driverPhone;
    private String paramedicName;
    private String paramedicPhone;
    private String currentLocation;
    private Integer fuelStatus;
    private Boolean isActive;
}
