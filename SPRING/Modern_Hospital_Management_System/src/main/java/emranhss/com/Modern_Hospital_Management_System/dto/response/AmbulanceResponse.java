package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmbulanceResponse {

    private Long id;
    private String ambulanceNumber;
    private String vehicleType;
    private String vehiclePlate;
    private String driverName;
    private String driverPhone;
    private String paramedicName;
    private String paramedicPhone;
    private String status;
    private String currentLocation;
    private Integer fuelStatus;
    private LocalDate lastServiceDate;
    private LocalDate nextServiceDate;
    private Boolean isActive;
    private String equipmentChecklist;
}
