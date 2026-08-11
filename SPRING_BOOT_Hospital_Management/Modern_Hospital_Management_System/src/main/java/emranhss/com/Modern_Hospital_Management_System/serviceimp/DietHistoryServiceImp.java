package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.entity.DietHistory;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.DietHistoryRepository;
import emranhss.com.Modern_Hospital_Management_System.service.DietHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DietHistoryServiceImp implements DietHistoryService {

    private final DietHistoryRepository dietHistoryRepository;

    @Override
    @Transactional
    public DietHistory create(DietHistory history) {
        return dietHistoryRepository.save(history);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietHistory> getAll() {
        return dietHistoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietHistory> getByPatientId(Long patientId) {
        return dietHistoryRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietHistory> getByDietAssignmentId(Long assignmentId) {
        return dietHistoryRepository.findByDietAssignmentIdOrderByCreatedAtDesc(assignmentId);
    }
}
