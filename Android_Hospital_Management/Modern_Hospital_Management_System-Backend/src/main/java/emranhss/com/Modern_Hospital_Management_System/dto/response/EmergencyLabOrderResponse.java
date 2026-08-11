package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyLabOrderResponse {

    private Long id;
    private Long emergencyPatientId;
    private String emergencyNumber;
    private String testName;
    private String orderType;
    private String orderedBy;
    private LocalDateTime orderedAt;
    private LocalDateTime sampleCollectionTime;
    private LocalDateTime resultTime;
    private String resultValue;
    private String status;
    private Boolean isCritical;
    private String priority;
    private String notes;
}
