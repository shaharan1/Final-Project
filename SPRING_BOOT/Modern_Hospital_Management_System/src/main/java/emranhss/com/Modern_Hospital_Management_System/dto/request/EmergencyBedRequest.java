package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyBedRequest {

    private Long emergencyPatientId;
    private String bedNumber;
    private String wardName;
    private String bedType;
}
