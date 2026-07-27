package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.entity.Insurance;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.InsuranceRepository;
import emranhss.com.Modern_Hospital_Management_System.service.InsuranceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InsuranceServiceImp implements InsuranceService {

    private final InsuranceRepository insuranceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Insurance> getAll() {
        return insuranceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Insurance getById(Long id) {
        return insuranceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Insurance> getActive() {
        return insuranceRepository.findByActive(true);
    }

    @Override
    @Transactional
    public Insurance create(Insurance insurance) {
        insurance.setActive(true);
        insurance.setCreatedDate(LocalDateTime.now());
        return insuranceRepository.save(insurance);
    }

    @Override
    @Transactional
    public Insurance update(Long id, Insurance insurance) {
        Insurance existing = insuranceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance not found with ID: " + id));
        existing.setCompanyName(insurance.getCompanyName());
        existing.setContactPerson(insurance.getContactPerson());
        existing.setPhone(insurance.getPhone());
        existing.setEmail(insurance.getEmail());
        existing.setAddress(insurance.getAddress());
        existing.setPolicyPrefix(insurance.getPolicyPrefix());
        existing.setCoveragePercentage(insurance.getCoveragePercentage());
        existing.setMaxCoverage(insurance.getMaxCoverage());
        existing.setActive(insurance.getActive());
        existing.setNotes(insurance.getNotes());
        existing.setUpdatedDate(LocalDateTime.now());
        return insuranceRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!insuranceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Insurance not found with ID: " + id);
        }
        insuranceRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Insurance> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return insuranceRepository.findAll();
        }
        return insuranceRepository.findByCompanyNameContainingIgnoreCase(keyword.trim());
    }
}
