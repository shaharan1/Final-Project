package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyLabOrderRequest {

    private Long emergencyPatientId;
    private String testName;
    private String orderType;
    private String orderedBy;
    private String priority;
    private String notes;
}
