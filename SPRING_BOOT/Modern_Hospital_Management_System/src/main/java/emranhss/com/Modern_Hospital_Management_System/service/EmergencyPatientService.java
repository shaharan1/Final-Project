package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyPatientRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyStatusUpdateRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyDashboardResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyPatientResponse;

import java.util.List;

public interface EmergencyPatientService {

    EmergencyPatientResponse create(EmergencyPatientRequest request);

    EmergencyPatientResponse getById(Long id);

    List<EmergencyPatientResponse> getAll();

    List<EmergencyPatientResponse> search(String keyword);

    EmergencyPatientResponse update(Long id, EmergencyPatientRequest request);

    EmergencyPatientResponse updateStatus(Long id, EmergencyStatusUpdateRequest request);

    void delete(Long id);

    EmergencyDashboardResponse getDashboard();

    List<EmergencyPatientResponse> getByStatus(String status);

    List<EmergencyPatientResponse> getByTriageLevel(Integer triageLevel);
}
