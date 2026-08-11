package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyDoctorAssignmentRequest {

    private Long emergencyPatientId;
    private Long doctorId;
    private Long nurseId;
    private String assignmentType;
    private String notes;
}
