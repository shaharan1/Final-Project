package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyPatientRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyPatientResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyPatient;
import emranhss.com.Modern_Hospital_Management_System.entity.Patient;
import emranhss.com.Modern_Hospital_Management_System.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmergencyPatientMapper {

    private final PatientRepository patientRepository;

    public EmergencyPatient toEntity(EmergencyPatientRequest request) {
        if (request == null) return null;
        EmergencyPatient entity = new EmergencyPatient();
        BeanUtils.copyProperties(request, entity);
        if (request.getPatientId() != null) {
            Patient patient = patientRepository.findById(request.getPatientId()).orElse(null);
            entity.setPatient(patient);
        }
        return entity;
    }

    public EmergencyPatientResponse toResponse(EmergencyPatient entity) {
        if (entity == null) return null;
        EmergencyPatientResponse response = new EmergencyPatientResponse();
        BeanUtils.copyProperties(entity, response);
        if (entity.getPatient() != null) {
            response.setPatientId(entity.getPatient().getId());
        }
        return response;
    }

    public void updateEntityFromRequest(EmergencyPatientRequest request, EmergencyPatient entity) {
        if (request == null || entity == null) return;
        BeanUtils.copyProperties(request, entity, "id", "emergencyNumber", "createdAt", "updatedAt");
        if (request.getPatientId() != null) {
            Patient patient = patientRepository.findById(request.getPatientId()).orElse(null);
            entity.setPatient(patient);
        }
    }
}
