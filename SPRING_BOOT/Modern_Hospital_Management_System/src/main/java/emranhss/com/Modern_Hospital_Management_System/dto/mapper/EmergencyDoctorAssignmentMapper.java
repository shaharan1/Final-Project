package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyDoctorAssignmentRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyDoctorAssignmentResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Doctor;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyDoctorAssignment;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyPatient;
import emranhss.com.Modern_Hospital_Management_System.entity.Nurse;
import emranhss.com.Modern_Hospital_Management_System.repository.DoctorRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.EmergencyPatientRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.NurseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmergencyDoctorAssignmentMapper {

    private final EmergencyPatientRepository emergencyPatientRepository;
    private final DoctorRepository doctorRepository;
    private final NurseRepository nurseRepository;

    public EmergencyDoctorAssignment toEntity(EmergencyDoctorAssignmentRequest request) {
        if (request == null) return null;
        EmergencyDoctorAssignment entity = new EmergencyDoctorAssignment();
        BeanUtils.copyProperties(request, entity);
        if (request.getEmergencyPatientId() != null) {
            EmergencyPatient patient = emergencyPatientRepository.findById(request.getEmergencyPatientId()).orElse(null);
            entity.setEmergencyPatient(patient);
        }
        if (request.getDoctorId() != null) {
            Doctor doctor = doctorRepository.findById(request.getDoctorId()).orElse(null);
            entity.setDoctor(doctor);
        }
        if (request.getNurseId() != null) {
            Nurse nurse = nurseRepository.findById(request.getNurseId()).orElse(null);
            entity.setNurse(nurse);
        }
        return entity;
    }

    public EmergencyDoctorAssignmentResponse toResponse(EmergencyDoctorAssignment entity) {
        if (entity == null) return null;
        EmergencyDoctorAssignmentResponse response = new EmergencyDoctorAssignmentResponse();
        BeanUtils.copyProperties(entity, response);
        if (entity.getEmergencyPatient() != null) {
            response.setEmergencyPatientId(entity.getEmergencyPatient().getId());
            response.setEmergencyNumber(entity.getEmergencyPatient().getEmergencyNumber());
        }
        if (entity.getDoctor() != null) {
            response.setDoctorId(entity.getDoctor().getId());
            if (entity.getDoctor().getUser() != null) {
                response.setDoctorName(entity.getDoctor().getUser().getName());
            }
        }
        if (entity.getNurse() != null) {
            response.setNurseId(entity.getNurse().getId());
            if (entity.getNurse().getUser() != null) {
                response.setNurseName(entity.getNurse().getUser().getName());
            }
        }
        return response;
    }

    public void updateEntityFromRequest(EmergencyDoctorAssignmentRequest request, EmergencyDoctorAssignment entity) {
        if (request == null || entity == null) return;
        BeanUtils.copyProperties(request, entity, "id", "createdAt", "updatedAt");
        if (request.getEmergencyPatientId() != null) {
            EmergencyPatient patient = emergencyPatientRepository.findById(request.getEmergencyPatientId()).orElse(null);
            entity.setEmergencyPatient(patient);
        }
        if (request.getDoctorId() != null) {
            Doctor doctor = doctorRepository.findById(request.getDoctorId()).orElse(null);
            entity.setDoctor(doctor);
        }
        if (request.getNurseId() != null) {
            Nurse nurse = nurseRepository.findById(request.getNurseId()).orElse(null);
            entity.setNurse(nurse);
        }
    }
}
