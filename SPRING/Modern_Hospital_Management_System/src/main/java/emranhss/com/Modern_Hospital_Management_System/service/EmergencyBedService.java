package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyBedRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyBedResponse;

import java.util.List;
import java.util.Map;

public interface EmergencyBedService {

    EmergencyBedResponse create(EmergencyBedRequest request);

    EmergencyBedResponse getById(Long id);

    List<EmergencyBedResponse> getAll();

    EmergencyBedResponse updateStatus(Long id, String status);

    EmergencyBedResponse assignBed(Long bedId, Long emergencyPatientId);

    EmergencyBedResponse releaseBed(Long bedId);

    List<EmergencyBedResponse> getByStatus(String status);

    List<EmergencyBedResponse> getByWardName(String wardName);

    Long getAvailableCount();

    Map<String, Object> getWardSummary();
}
