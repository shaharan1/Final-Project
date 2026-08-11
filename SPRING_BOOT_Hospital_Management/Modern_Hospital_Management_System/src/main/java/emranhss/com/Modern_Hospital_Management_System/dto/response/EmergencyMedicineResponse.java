package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyMedicineResponse {

    private Long id;
    private Long emergencyPatientId;
    private String emergencyNumber;
    private String medicineName;
    private String dose;
    private String route;
    private String frequency;
    private Integer quantity;
    private Boolean stockAvailable;
    private String administeredBy;
    private LocalDateTime administeredAt;
    private Boolean pharmacyRequestSent;
    private String status;
    private String notes;
}
