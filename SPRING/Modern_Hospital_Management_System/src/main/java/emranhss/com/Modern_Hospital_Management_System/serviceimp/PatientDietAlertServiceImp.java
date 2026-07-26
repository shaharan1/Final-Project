package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.entity.PatientDietAlert;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.PatientDietAlertRepository;
import emranhss.com.Modern_Hospital_Management_System.service.PatientDietAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientDietAlertServiceImp implements PatientDietAlertService {

    private final PatientDietAlertRepository patientDietAlertRepository;

    @Override
    @Transactional
    public PatientDietAlert create(PatientDietAlert alert) {
        return patientDietAlertRepository.save(alert);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientDietAlert getById(Long id) {
        return patientDietAlertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PatientDietAlert not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientDietAlert> getAll() {
        return patientDietAlertRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientDietAlert> getActive() {
        return patientDietAlertRepository.findByStatusOrderByCreatedAtDesc("ACTIVE");
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientDietAlert> getByPatientId(Long patientId) {
        return patientDietAlertRepository.findByPatientId(patientId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientDietAlert> getByAlertType(String alertType) {
        return patientDietAlertRepository.findByAlertType(alertType);
    }

    @Override
    @Transactional
    public PatientDietAlert updateStatus(Long id, String status) {
        PatientDietAlert existing = patientDietAlertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PatientDietAlert not found with id: " + id));
        existing.setStatus(status);
        if ("RESOLVED".equals(status)) {
            existing.setResolvedAt(LocalDateTime.now());
        }
        return patientDietAlertRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        PatientDietAlert existing = patientDietAlertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PatientDietAlert not found with id: " + id));
        existing.setStatus("RESOLVED");
        existing.setResolvedAt(LocalDateTime.now());
        patientDietAlertRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public long getActiveCount() {
        return patientDietAlertRepository.countByStatus("ACTIVE");
    }
}
