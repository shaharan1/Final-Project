package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyTimelineRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyTimelineResponse;

import java.util.List;

public interface EmergencyTimelineService {

    List<EmergencyTimelineResponse> getByEmergencyPatientId(Long emergencyPatientId);

    List<EmergencyTimelineResponse> getAll();

    EmergencyTimelineResponse addEvent(Long emergencyPatientId, EmergencyTimelineRequest request);

    List<EmergencyTimelineResponse> getRecentEvents();
}
