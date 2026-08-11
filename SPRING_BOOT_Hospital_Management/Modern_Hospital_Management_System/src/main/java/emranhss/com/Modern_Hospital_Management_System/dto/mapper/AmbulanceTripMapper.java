package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.AmbulanceTripRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.AmbulanceTripResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Ambulance;
import emranhss.com.Modern_Hospital_Management_System.entity.AmbulanceTrip;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyPatient;
import emranhss.com.Modern_Hospital_Management_System.repository.AmbulanceRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.EmergencyPatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AmbulanceTripMapper {

    private final AmbulanceRepository ambulanceRepository;
    private final EmergencyPatientRepository emergencyPatientRepository;

    public AmbulanceTrip toEntity(AmbulanceTripRequest request) {
        if (request == null) return null;
        AmbulanceTrip entity = new AmbulanceTrip();
        BeanUtils.copyProperties(request, entity);
        if (request.getAmbulanceId() != null) {
            Ambulance ambulance = ambulanceRepository.findById(request.getAmbulanceId()).orElse(null);
            entity.setAmbulance(ambulance);
        }
        if (request.getEmergencyPatientId() != null) {
            EmergencyPatient patient = emergencyPatientRepository.findById(request.getEmergencyPatientId()).orElse(null);
            entity.setEmergencyPatient(patient);
        }
        return entity;
    }

    public AmbulanceTripResponse toResponse(AmbulanceTrip entity) {
        if (entity == null) return null;
        AmbulanceTripResponse response = new AmbulanceTripResponse();
        BeanUtils.copyProperties(entity, response);
        if (entity.getAmbulance() != null) {
            response.setAmbulanceId(entity.getAmbulance().getId());
            response.setAmbulanceNumber(entity.getAmbulance().getAmbulanceNumber());
        }
        if (entity.getEmergencyPatient() != null) {
            response.setEmergencyPatientId(entity.getEmergencyPatient().getId());
            response.setEmergencyNumber(entity.getEmergencyPatient().getEmergencyNumber());
        }
        return response;
    }

    public void updateEntityFromRequest(AmbulanceTripRequest request, AmbulanceTrip entity) {
        if (request == null || entity == null) return;
        BeanUtils.copyProperties(request, entity, "id", "createdAt", "updatedAt");
        if (request.getAmbulanceId() != null) {
            Ambulance ambulance = ambulanceRepository.findById(request.getAmbulanceId()).orElse(null);
            entity.setAmbulance(ambulance);
        }
        if (request.getEmergencyPatientId() != null) {
            EmergencyPatient patient = emergencyPatientRepository.findById(request.getEmergencyPatientId()).orElse(null);
            entity.setEmergencyPatient(patient);
        }
    }
}
