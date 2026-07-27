package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyBedResponse {

    private Long id;
    private String bedNumber;
    private String wardName;
    private String bedType;
    private String status;
    private Long emergencyPatientId;
    private String emergencyNumber;
    private LocalDateTime assignedAt;
    private LocalDateTime releasedAt;
    private Boolean isActive;
}
