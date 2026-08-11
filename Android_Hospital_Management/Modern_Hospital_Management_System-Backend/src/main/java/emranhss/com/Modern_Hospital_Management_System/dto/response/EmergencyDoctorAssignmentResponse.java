package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyDoctorAssignmentResponse {

    private Long id;
    private Long emergencyPatientId;
    private String emergencyNumber;
    private Long doctorId;
    private String doctorName;
    private Long nurseId;
    private String nurseName;
    private String assignmentType;
    private LocalDateTime assignedAt;
    private LocalDateTime unassignedAt;
    private Boolean isActive;
    private String notes;
}
