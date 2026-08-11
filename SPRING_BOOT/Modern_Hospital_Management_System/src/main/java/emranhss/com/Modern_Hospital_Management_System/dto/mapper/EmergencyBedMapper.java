package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyBedRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyBedResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyBed;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyPatient;
import emranhss.com.Modern_Hospital_Management_System.repository.EmergencyPatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmergencyBedMapper {

    private final EmergencyPatientRepository emergencyPatientRepository;

    public EmergencyBed toEntity(EmergencyBedRequest request) {
        if (request == null) return null;
        EmergencyBed entity = new EmergencyBed();
        BeanUtils.copyProperties(request, entity);
        if (request.getEmergencyPatientId() != null) {
            EmergencyPatient patient = emergencyPatientRepository.findById(request.getEmergencyPatientId()).orElse(null);
            entity.setEmergencyPatient(patient);
        }
        return entity;
    }

    public EmergencyBedResponse toResponse(EmergencyBed entity) {
        if (entity == null) return null;
        EmergencyBedResponse response = new EmergencyBedResponse();
        BeanUtils.copyProperties(entity, response);
        if (entity.getEmergencyPatient() != null) {
            response.setEmergencyPatientId(entity.getEmergencyPatient().getId());
            response.setEmergencyNumber(entity.getEmergencyPatient().getEmergencyNumber());
        }
        return response;
    }

    public void updateEntityFromRequest(EmergencyBedRequest request, EmergencyBed entity) {
        if (request == null || entity == null) return;
        BeanUtils.copyProperties(request, entity, "id", "createdAt", "updatedAt");
        if (request.getEmergencyPatientId() != null) {
            EmergencyPatient patient = emergencyPatientRepository.findById(request.getEmergencyPatientId()).orElse(null);
            entity.setEmergencyPatient(patient);
        }
    }
}
