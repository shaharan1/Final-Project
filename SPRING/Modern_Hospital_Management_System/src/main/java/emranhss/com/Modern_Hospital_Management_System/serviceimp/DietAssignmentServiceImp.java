package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.entity.DietAssignment;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.DietAssignmentRepository;
import emranhss.com.Modern_Hospital_Management_System.service.DietAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DietAssignmentServiceImp implements DietAssignmentService {

    private final DietAssignmentRepository dietAssignmentRepository;

    @Override
    @Transactional
    public DietAssignment create(DietAssignment assignment) {
        return dietAssignmentRepository.save(assignment);
    }

    @Override
    @Transactional(readOnly = true)
    public DietAssignment getById(Long id) {
        return dietAssignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DietAssignment not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietAssignment> getAll() {
        return dietAssignmentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietAssignment> getByStatus(String status) {
        return dietAssignmentRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietAssignment> getByPatientId(Long patientId) {
        return dietAssignmentRepository.findByPatientId(patientId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietAssignment> getByAdmittedPatientId(Long admittedPatientId) {
        return dietAssignmentRepository.findByAdmittedPatientId(admittedPatientId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietAssignment> getByDieticianId(Long dieticianId) {
        return dietAssignmentRepository.findByDieticianId(dieticianId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietAssignment> getActiveAssignments() {
        return dietAssignmentRepository.findActiveAssignments(LocalDate.now());
    }

    @Override
    @Transactional
    public DietAssignment update(Long id, DietAssignment assignment) {
        DietAssignment existing = dietAssignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DietAssignment not found with id: " + id));
        existing.setPatient(assignment.getPatient());
        existing.setAdmittedPatient(assignment.getAdmittedPatient());
        existing.setDietPlan(assignment.getDietPlan());
        existing.setAssignedByDoctor(assignment.getAssignedByDoctor());
        existing.setDietician(assignment.getDietician());
        existing.setStartDate(assignment.getStartDate());
        existing.setEndDate(assignment.getEndDate());
        existing.setStatus(assignment.getStatus());
        existing.setReason(assignment.getReason());
        existing.setSpecialInstructions(assignment.getSpecialInstructions());
        existing.setTargetCalories(assignment.getTargetCalories());
        existing.setTargetWeight(assignment.getTargetWeight());
        return dietAssignmentRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        DietAssignment existing = dietAssignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DietAssignment not found with id: " + id));
        existing.setStatus("CANCELLED");
        dietAssignmentRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public long getActiveCount() {
        return dietAssignmentRepository.countByStatus("ACTIVE");
    }
}
