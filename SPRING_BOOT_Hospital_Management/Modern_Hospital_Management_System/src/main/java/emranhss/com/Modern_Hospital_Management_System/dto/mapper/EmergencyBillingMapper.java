package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyBillingRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyBillingResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyBilling;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyPatient;
import emranhss.com.Modern_Hospital_Management_System.repository.EmergencyPatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmergencyBillingMapper {

    private final EmergencyPatientRepository emergencyPatientRepository;

    public EmergencyBilling toEntity(EmergencyBillingRequest request) {
        if (request == null) return null;
        EmergencyBilling entity = new EmergencyBilling();
        BeanUtils.copyProperties(request, entity);
        if (request.getEmergencyPatientId() != null) {
            EmergencyPatient patient = emergencyPatientRepository.findById(request.getEmergencyPatientId()).orElse(null);
            entity.setEmergencyPatient(patient);
        }
        return entity;
    }

    public EmergencyBillingResponse toResponse(EmergencyBilling entity) {
        if (entity == null) return null;
        EmergencyBillingResponse response = new EmergencyBillingResponse();
        BeanUtils.copyProperties(entity, response);
        if (entity.getEmergencyPatient() != null) {
            response.setEmergencyPatientId(entity.getEmergencyPatient().getId());
            response.setEmergencyNumber(entity.getEmergencyPatient().getEmergencyNumber());
        }
        return response;
    }

    public void updateEntityFromRequest(EmergencyBillingRequest request, EmergencyBilling entity) {
        if (request == null || entity == null) return;
        BeanUtils.copyProperties(request, entity, "id", "billNumber", "createdAt", "updatedAt");
        if (request.getEmergencyPatientId() != null) {
            EmergencyPatient patient = emergencyPatientRepository.findById(request.getEmergencyPatientId()).orElse(null);
            entity.setEmergencyPatient(patient);
        }
    }
}
