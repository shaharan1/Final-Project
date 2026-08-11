package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyTimelineResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyPatient;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyTimeline;
import emranhss.com.Modern_Hospital_Management_System.repository.EmergencyPatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmergencyTimelineMapper {

    private final EmergencyPatientRepository emergencyPatientRepository;

    public EmergencyTimeline toEntity(Long emergencyPatientId, String eventType, String description,
                                       String performedBy, String department) {
        EmergencyTimeline entity = new EmergencyTimeline();
        EmergencyPatient patient = emergencyPatientRepository.findById(emergencyPatientId).orElse(null);
        entity.setEmergencyPatient(patient);
        entity.setEventType(eventType);
        entity.setDescription(description);
        entity.setPerformedBy(performedBy);
        entity.setDepartment(department);
        entity.setStatus("COMPLETED");
        return entity;
    }

    public EmergencyTimelineResponse toResponse(EmergencyTimeline entity) {
        if (entity == null) return null;
        EmergencyTimelineResponse response = new EmergencyTimelineResponse();
        BeanUtils.copyProperties(entity, response);
        if (entity.getEmergencyPatient() != null) {
            response.setEmergencyPatientId(entity.getEmergencyPatient().getId());
            response.setEmergencyNumber(entity.getEmergencyPatient().getEmergencyNumber());
        }
        return response;
    }
}
