package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.TriageRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.TriageResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyPatient;
import emranhss.com.Modern_Hospital_Management_System.entity.Triage;
import emranhss.com.Modern_Hospital_Management_System.repository.EmergencyPatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TriageMapper {

    private final EmergencyPatientRepository emergencyPatientRepository;

    public Triage toEntity(TriageRequest request) {
        if (request == null) return null;
        Triage entity = new Triage();
        BeanUtils.copyProperties(request, entity);
        if (request.getEmergencyPatientId() != null) {
            EmergencyPatient patient = emergencyPatientRepository.findById(request.getEmergencyPatientId()).orElse(null);
            entity.setEmergencyPatient(patient);
        }
        return entity;
    }

    public TriageResponse toResponse(Triage entity) {
        if (entity == null) return null;
        TriageResponse response = new TriageResponse();
        BeanUtils.copyProperties(entity, response);
        if (entity.getEmergencyPatient() != null) {
            response.setEmergencyPatientId(entity.getEmergencyPatient().getId());
            response.setEmergencyNumber(entity.getEmergencyPatient().getEmergencyNumber());
        }
        return response;
    }

    public void updateEntityFromRequest(TriageRequest request, Triage entity) {
        if (request == null || entity == null) return;
        BeanUtils.copyProperties(request, entity, "id", "createdAt", "updatedAt");
        if (request.getEmergencyPatientId() != null) {
            EmergencyPatient patient = emergencyPatientRepository.findById(request.getEmergencyPatientId()).orElse(null);
            entity.setEmergencyPatient(patient);
        }
    }
}
