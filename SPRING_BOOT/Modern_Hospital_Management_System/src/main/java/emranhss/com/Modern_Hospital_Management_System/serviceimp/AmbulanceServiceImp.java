package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.AmbulanceMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.AmbulanceRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.AmbulanceResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Ambulance;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.AmbulanceRepository;
import emranhss.com.Modern_Hospital_Management_System.service.AmbulanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AmbulanceServiceImp implements AmbulanceService {

    private final AmbulanceRepository ambulanceRepository;
    private final AmbulanceMapper ambulanceMapper;

    @Override
    @Transactional
    public AmbulanceResponse create(AmbulanceRequest request) {
        Ambulance ambulance = ambulanceMapper.toEntity(request);
        ambulance.setStatus("AVAILABLE");
        Ambulance saved = ambulanceRepository.save(ambulance);
        return ambulanceMapper.toResponse(saved);
    }

    @Override
    public AmbulanceResponse getById(Long id) {
        Ambulance ambulance = ambulanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ambulance not found with id: " + id));
        return ambulanceMapper.toResponse(ambulance);
    }

    @Override
    public List<AmbulanceResponse> getAll() {
        return ambulanceRepository.findAll().stream()
                .map(ambulanceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AmbulanceResponse update(Long id, AmbulanceRequest request) {
        Ambulance ambulance = ambulanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ambulance not found with id: " + id));
        ambulanceMapper.updateEntityFromRequest(request, ambulance);
        return ambulanceMapper.toResponse(ambulanceRepository.save(ambulance));
    }

    @Override
    @Transactional
    public AmbulanceResponse updateStatus(Long id, String status) {
        Ambulance ambulance = ambulanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ambulance not found with id: " + id));
        ambulance.setStatus(status);
        return ambulanceMapper.toResponse(ambulanceRepository.save(ambulance));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Ambulance ambulance = ambulanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ambulance not found with id: " + id));
        ambulanceRepository.delete(ambulance);
    }

    @Override
    public List<AmbulanceResponse> getByStatus(String status) {
        return ambulanceRepository.findByStatus(status).stream()
                .map(ambulanceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Long getAvailableCount() {
        return ambulanceRepository.countByStatus("AVAILABLE");
    }
}
