package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.entity.Dietician;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.DieticianRepository;
import emranhss.com.Modern_Hospital_Management_System.service.DieticianService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DieticianServiceImp implements DieticianService {

    private final DieticianRepository dieticianRepository;

    @Override
    @Transactional
    public Dietician create(Dietician dietician) {
        return dieticianRepository.save(dietician);
    }

    @Override
    @Transactional(readOnly = true)
    public Dietician getById(Long id) {
        return dieticianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dietician not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Dietician> getAll() {
        return dieticianRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Dietician> getActive() {
        return dieticianRepository.findByActiveTrue();
    }

    @Override
    @Transactional
    public Dietician update(Long id, Dietician dietician) {
        Dietician existing = dieticianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dietician not found with id: " + id));
        existing.setSpecialization(dietician.getSpecialization());
        existing.setQualification(dietician.getQualification());
        existing.setExperienceYears(dietician.getExperienceYears());
        existing.setLicenseNumber(dietician.getLicenseNumber());
        existing.setPhone(dietician.getPhone());
        existing.setAvailableDays(dietician.getAvailableDays());
        existing.setDutyHours(dietician.getDutyHours());
        existing.setActive(dietician.getActive());
        return dieticianRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Dietician existing = dieticianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dietician not found with id: " + id));
        existing.setActive(false);
        dieticianRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Dietician> search(String keyword) {
        return dieticianRepository.search(keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public long getActiveCount() {
        return dieticianRepository.countByActiveTrue();
    }
}
