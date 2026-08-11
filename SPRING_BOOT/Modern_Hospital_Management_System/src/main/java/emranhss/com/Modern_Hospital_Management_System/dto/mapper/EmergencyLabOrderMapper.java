package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyLabOrderRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyLabOrderResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyLabOrder;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyPatient;
import emranhss.com.Modern_Hospital_Management_System.repository.EmergencyPatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmergencyLabOrderMapper {

    private final EmergencyPatientRepository emergencyPatientRepository;

    public EmergencyLabOrder toEntity(EmergencyLabOrderRequest request) {
        if (request == null) return null;
        EmergencyLabOrder entity = new EmergencyLabOrder();
        BeanUtils.copyProperties(request, entity);
        if (request.getEmergencyPatientId() != null) {
            EmergencyPatient patient = emergencyPatientRepository.findById(request.getEmergencyPatientId()).orElse(null);
            entity.setEmergencyPatient(patient);
        }
        return entity;
    }

    public EmergencyLabOrderResponse toResponse(EmergencyLabOrder entity) {
        if (entity == null) return null;
        EmergencyLabOrderResponse response = new EmergencyLabOrderResponse();
        BeanUtils.copyProperties(entity, response);
        if (entity.getEmergencyPatient() != null) {
            response.setEmergencyPatientId(entity.getEmergencyPatient().getId());
            response.setEmergencyNumber(entity.getEmergencyPatient().getEmergencyNumber());
        }
        return response;
    }

    public void updateEntityFromRequest(EmergencyLabOrderRequest request, EmergencyLabOrder entity) {
        if (request == null || entity == null) return;
        BeanUtils.copyProperties(request, entity, "id", "createdAt", "updatedAt");
        if (request.getEmergencyPatientId() != null) {
            EmergencyPatient patient = emergencyPatientRepository.findById(request.getEmergencyPatientId()).orElse(null);
            entity.setEmergencyPatient(patient);
        }
    }
}
