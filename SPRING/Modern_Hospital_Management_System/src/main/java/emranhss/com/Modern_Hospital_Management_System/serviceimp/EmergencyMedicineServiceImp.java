package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.EmergencyMedicineMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyMedicineRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyMedicineResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyMedicine;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.EmergencyMedicineRepository;
import emranhss.com.Modern_Hospital_Management_System.service.EmergencyMedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmergencyMedicineServiceImp implements EmergencyMedicineService {

    private final EmergencyMedicineRepository emergencyMedicineRepository;
    private final EmergencyMedicineMapper emergencyMedicineMapper;

    @Override
    @Transactional
    public EmergencyMedicineResponse create(EmergencyMedicineRequest request) {
        EmergencyMedicine medicine = emergencyMedicineMapper.toEntity(request);
        medicine.setStatus("PRESCRIBED");
        medicine.setPharmacyRequestSent(false);
        EmergencyMedicine saved = emergencyMedicineRepository.save(medicine);
        return emergencyMedicineMapper.toResponse(saved);
    }

    @Override
    public List<EmergencyMedicineResponse> getByEmergencyPatientId(Long emergencyPatientId) {
        return emergencyMedicineRepository.findByEmergencyPatientId(emergencyPatientId).stream()
                .map(emergencyMedicineMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmergencyMedicineResponse> getAll() {
        return emergencyMedicineRepository.findAll().stream()
                .map(emergencyMedicineMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmergencyMedicineResponse updateStatus(Long id, String status) {
        EmergencyMedicine medicine = emergencyMedicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency medicine not found with id: " + id));
        medicine.setStatus(status);
        if ("ADMINISTERED".equals(status)) {
            medicine.setAdministeredAt(LocalDateTime.now());
        }
        return emergencyMedicineMapper.toResponse(emergencyMedicineRepository.save(medicine));
    }

    @Override
    @Transactional
    public EmergencyMedicineResponse requestPharmacy(Long id) {
        EmergencyMedicine medicine = emergencyMedicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency medicine not found with id: " + id));
        medicine.setPharmacyRequestSent(true);
        medicine.setPharmacyRequestTime(LocalDateTime.now());
        medicine.setStatus("PHARMACY_REQUESTED");
        return emergencyMedicineMapper.toResponse(emergencyMedicineRepository.save(medicine));
    }

    @Override
    public List<EmergencyMedicineResponse> getByStatus(String status) {
        return emergencyMedicineRepository.findByStatus(status).stream()
                .map(emergencyMedicineMapper::toResponse)
                .collect(Collectors.toList());
    }
}
