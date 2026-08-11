package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyMedicineRequest {

    private Long emergencyPatientId;
    private String medicineName;
    private String dose;
    private String route;
    private String frequency;
    private Integer quantity;
    private String administeredBy;
    private String notes;
}
