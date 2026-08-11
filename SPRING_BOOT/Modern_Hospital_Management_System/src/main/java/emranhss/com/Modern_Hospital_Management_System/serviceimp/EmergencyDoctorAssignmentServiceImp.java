package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.EmergencyDoctorAssignmentMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyDoctorAssignmentRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyDoctorAssignmentResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyDoctorAssignment;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyPatient;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.EmergencyDoctorAssignmentRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.EmergencyPatientRepository;
import emranhss.com.Modern_Hospital_Management_System.service.EmergencyDoctorAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmergencyDoctorAssignmentServiceImp implements EmergencyDoctorAssignmentService {

    private final EmergencyDoctorAssignmentRepository emergencyDoctorAssignmentRepository;
    private final EmergencyDoctorAssignmentMapper emergencyDoctorAssignmentMapper;
    private final EmergencyPatientRepository emergencyPatientRepository;

    @Override
    @Transactional
    public EmergencyDoctorAssignmentResponse create(EmergencyDoctorAssignmentRequest request) {
        EmergencyDoctorAssignment assignment = emergencyDoctorAssignmentMapper.toEntity(request);
        assignment.setIsActive(true);
        EmergencyDoctorAssignment saved = emergencyDoctorAssignmentRepository.save(assignment);

        if (request.getEmergencyPatientId() != null) {
            EmergencyPatient patient = emergencyPatientRepository.findById(request.getEmergencyPatientId())
                    .orElse(null);
            if (patient != null) {
                patient.setStatus("UNDER_TREATMENT");
                patient.setDoctorAssignedTime(LocalDateTime.now());
                emergencyPatientRepository.save(patient);
            }
        }

        return emergencyDoctorAssignmentMapper.toResponse(saved);
    }

    @Override
    public List<EmergencyDoctorAssignmentResponse> getByEmergencyPatientId(Long emergencyPatientId) {
        return emergencyDoctorAssignmentRepository.findByEmergencyPatientId(emergencyPatientId).stream()
                .map(emergencyDoctorAssignmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmergencyDoctorAssignmentResponse> getAll() {
        return emergencyDoctorAssignmentRepository.findAll().stream()
                .map(emergencyDoctorAssignmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmergencyDoctorAssignmentResponse unassign(Long id) {
        EmergencyDoctorAssignment assignment = emergencyDoctorAssignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor assignment not found with id: " + id));
        assignment.setIsActive(false);
        assignment.setUnassignedAt(LocalDateTime.now());
        return emergencyDoctorAssignmentMapper.toResponse(emergencyDoctorAssignmentRepository.save(assignment));
    }

    @Override
    public List<EmergencyDoctorAssignmentResponse> getActiveAssignments() {
        return emergencyDoctorAssignmentRepository.findByIsActive(true).stream()
                .map(emergencyDoctorAssignmentMapper::toResponse)
                .collect(Collectors.toList());
    }
}
