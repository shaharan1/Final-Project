package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyMedicineRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyMedicineResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyMedicine;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyPatient;
import emranhss.com.Modern_Hospital_Management_System.repository.EmergencyPatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmergencyMedicineMapper {

    private final EmergencyPatientRepository emergencyPatientRepository;

    public EmergencyMedicine toEntity(EmergencyMedicineRequest request) {
        if (request == null) return null;
        EmergencyMedicine entity = new EmergencyMedicine();
        BeanUtils.copyProperties(request, entity);
        if (request.getEmergencyPatientId() != null) {
            EmergencyPatient patient = emergencyPatientRepository.findById(request.getEmergencyPatientId()).orElse(null);
            entity.setEmergencyPatient(patient);
        }
        return entity;
    }

    public EmergencyMedicineResponse toResponse(EmergencyMedicine entity) {
        if (entity == null) return null;
        EmergencyMedicineResponse response = new EmergencyMedicineResponse();
        BeanUtils.copyProperties(entity, response);
        if (entity.getEmergencyPatient() != null) {
            response.setEmergencyPatientId(entity.getEmergencyPatient().getId());
            response.setEmergencyNumber(entity.getEmergencyPatient().getEmergencyNumber());
        }
        return response;
    }

    public void updateEntityFromRequest(EmergencyMedicineRequest request, EmergencyMedicine entity) {
        if (request == null || entity == null) return;
        BeanUtils.copyProperties(request, entity, "id", "createdAt", "updatedAt");
        if (request.getEmergencyPatientId() != null) {
            EmergencyPatient patient = emergencyPatientRepository.findById(request.getEmergencyPatientId()).orElse(null);
            entity.setEmergencyPatient(patient);
        }
    }
}
