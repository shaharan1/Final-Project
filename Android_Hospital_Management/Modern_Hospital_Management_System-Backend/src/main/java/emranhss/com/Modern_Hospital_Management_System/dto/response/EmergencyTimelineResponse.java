package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyTimelineResponse {

    private Long id;
    private Long emergencyPatientId;
    private String emergencyNumber;
    private String eventType;
    private LocalDateTime eventTime;
    private String description;
    private String performedBy;
    private String department;
    private String status;
    private String notes;
}
