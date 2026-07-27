package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.TriageRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.TriageResponse;

import java.util.List;
import java.util.Map;

public interface TriageService {

    TriageResponse create(TriageRequest request);

    TriageResponse getByEmergencyPatientId(Long emergencyPatientId);

    List<TriageResponse> getAll();

    TriageResponse update(Long id, TriageRequest request);

    Map<String, Object> getTriageDistribution();
}
