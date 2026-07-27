package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyDoctorAssignmentRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyDoctorAssignmentResponse;

import java.util.List;

public interface EmergencyDoctorAssignmentService {

    EmergencyDoctorAssignmentResponse create(EmergencyDoctorAssignmentRequest request);

    List<EmergencyDoctorAssignmentResponse> getByEmergencyPatientId(Long emergencyPatientId);

    List<EmergencyDoctorAssignmentResponse> getAll();

    EmergencyDoctorAssignmentResponse unassign(Long id);

    List<EmergencyDoctorAssignmentResponse> getActiveAssignments();
}
