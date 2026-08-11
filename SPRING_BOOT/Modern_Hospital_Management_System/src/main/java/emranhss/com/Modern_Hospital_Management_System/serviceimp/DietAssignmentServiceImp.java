package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.entity.*;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.BedBookingRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.DietAssignmentRepository;
import emranhss.com.Modern_Hospital_Management_System.service.DietAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DietAssignmentServiceImp implements DietAssignmentService {

    private final DietAssignmentRepository dietAssignmentRepository;
    private final BedBookingRepository bedBookingRepository;

    private void enrichAssignment(DietAssignment a) {
        if (a.getAdmittedPatient() != null) {
            bedBookingRepository.findByAdmittedPatientIdAndActiveTrue(a.getAdmittedPatient().getId())
                    .ifPresent(booking -> {
                        if (booking.getBed() != null) {
                            a.setBedNumber(booking.getBed().getBedNumber());
                            if (booking.getBed().getWard() != null) {
                                a.setWardName(booking.getBed().getWard().getName());
                            }
                        }
                    });
        }
        if (a.getDietPlan() != null && a.getStartDate() != null) {
            double pricePerDay = a.getDietPlan().getPricePerDay() != null ? a.getDietPlan().getPricePerDay() : 0.0;
            LocalDate end = a.getEndDate() != null && a.getEndDate().isBefore(LocalDate.now()) ? a.getEndDate() : LocalDate.now();
            long days = ChronoUnit.DAYS.between(a.getStartDate(), end) + 1;
            if (days < 1) days = 1;
            a.setTotalAmount(pricePerDay * days);
        }
    }

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
        List<DietAssignment> list = dietAssignmentRepository.findAll();
        list.forEach(this::enrichAssignment);
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietAssignment> getByStatus(String status) {
        List<DietAssignment> list = dietAssignmentRepository.findByStatus(status);
        list.forEach(this::enrichAssignment);
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietAssignment> getByPatientId(Long patientId) {
        List<DietAssignment> list = dietAssignmentRepository.findByPatientId(patientId);
        list.forEach(this::enrichAssignment);
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietAssignment> getByAdmittedPatientId(Long admittedPatientId) {
        List<DietAssignment> list = dietAssignmentRepository.findByAdmittedPatientId(admittedPatientId);
        list.forEach(this::enrichAssignment);
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietAssignment> getByDieticianId(Long dieticianId) {
        List<DietAssignment> list = dietAssignmentRepository.findByDieticianId(dieticianId);
        list.forEach(this::enrichAssignment);
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietAssignment> getActiveAssignments() {
        List<DietAssignment> list = dietAssignmentRepository.findActiveAssignments(LocalDate.now());
        list.forEach(this::enrichAssignment);
        return list;
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
