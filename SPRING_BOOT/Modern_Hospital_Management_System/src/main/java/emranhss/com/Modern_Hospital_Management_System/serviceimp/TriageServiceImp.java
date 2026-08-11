package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.TriageMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.TriageRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.TriageResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyPatient;
import emranhss.com.Modern_Hospital_Management_System.entity.Triage;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.EmergencyPatientRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.TriageRepository;
import emranhss.com.Modern_Hospital_Management_System.service.TriageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TriageServiceImp implements TriageService {

    private final TriageRepository triageRepository;
    private final TriageMapper triageMapper;
    private final EmergencyPatientRepository emergencyPatientRepository;

    @Override
    @Transactional
    public TriageResponse create(TriageRequest request) {
        Triage triage = triageMapper.toEntity(request);
        if (request.getTriageLevel() != null) {
            triage.setTriageColor(getTriageColor(request.getTriageLevel()));
        }
        Triage saved = triageRepository.save(triage);

        if (request.getEmergencyPatientId() != null) {
            EmergencyPatient patient = emergencyPatientRepository.findById(request.getEmergencyPatientId())
                    .orElse(null);
            if (patient != null) {
                patient.setTriageLevel(request.getTriageLevel());
                patient.setTriageTime(LocalDateTime.now());
                if (request.getTriageLevel() != null) {
                    switch (request.getTriageLevel()) {
                        case 1:
                            patient.setStatus("CRITICAL");
                            patient.setSeverityLevel("CRITICAL");
                            break;
                        case 2:
                            patient.setStatus("CRITICAL");
                            patient.setSeverityLevel("SEVERE");
                            break;
                        case 3:
                            patient.setStatus("TRIAGE_PENDING");
                            patient.setSeverityLevel("MODERATE");
                            break;
                        case 4:
                            patient.setStatus("TRIAGE_PENDING");
                            patient.setSeverityLevel("MILD");
                            break;
                        case 5:
                            patient.setStatus("TRIAGE_PENDING");
                            patient.setSeverityLevel("MINOR");
                            break;
                        default:
                            patient.setStatus("TRIAGE_PENDING");
                            break;
                    }
                }
                emergencyPatientRepository.save(patient);
            }
        }

        return triageMapper.toResponse(saved);
    }

    @Override
    public TriageResponse getByEmergencyPatientId(Long emergencyPatientId) {
        Triage triage = triageRepository.findByEmergencyPatientId(emergencyPatientId)
                .orElseThrow(() -> new ResourceNotFoundException("No triage records found for patient id: " + emergencyPatientId));
        return triageMapper.toResponse(triage);
    }

    @Override
    public List<TriageResponse> getAll() {
        return triageRepository.findAll().stream()
                .map(triageMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TriageResponse update(Long id, TriageRequest request) {
        Triage triage = triageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Triage record not found with id: " + id));
        triageMapper.updateEntityFromRequest(request, triage);
        if (request.getTriageLevel() != null) {
            triage.setTriageColor(getTriageColor(request.getTriageLevel()));
        }
        return triageMapper.toResponse(triageRepository.save(triage));
    }

    @Override
    public Map<String, Object> getTriageDistribution() {
        Map<String, Object> distribution = new LinkedHashMap<>();
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> levelData = new LinkedHashMap<>();
            long count = triageRepository.countByTriageLevel(i);
            levelData.put("count", count);
            levelData.put("color", getTriageColor(i));
            distribution.put("Level " + i, levelData);
        }
        return distribution;
    }

    private String getTriageColor(Integer level) {
        if (level == null) return "GREEN";
        switch (level) {
            case 1: return "RED";
            case 2: return "ORANGE";
            case 3: return "YELLOW";
            case 4: return "GREEN";
            case 5: return "BLUE";
            default: return "GREEN";
        }
    }
}
