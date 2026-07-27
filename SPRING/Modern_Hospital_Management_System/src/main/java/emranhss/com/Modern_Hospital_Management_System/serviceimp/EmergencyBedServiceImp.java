package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.EmergencyBedMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyBedRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyBedResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyBed;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyPatient;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.EmergencyBedRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.EmergencyPatientRepository;
import emranhss.com.Modern_Hospital_Management_System.service.EmergencyBedService;
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
public class EmergencyBedServiceImp implements EmergencyBedService {

    private final EmergencyBedRepository emergencyBedRepository;
    private final EmergencyBedMapper emergencyBedMapper;
    private final EmergencyPatientRepository emergencyPatientRepository;

    @Override
    @Transactional
    public EmergencyBedResponse create(EmergencyBedRequest request) {
        EmergencyBed bed = emergencyBedMapper.toEntity(request);
        bed.setStatus("AVAILABLE");
        bed.setIsActive(true);
        EmergencyBed saved = emergencyBedRepository.save(bed);
        return emergencyBedMapper.toResponse(saved);
    }

    @Override
    public EmergencyBedResponse getById(Long id) {
        EmergencyBed bed = emergencyBedRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency bed not found with id: " + id));
        return emergencyBedMapper.toResponse(bed);
    }

    @Override
    public List<EmergencyBedResponse> getAll() {
        return emergencyBedRepository.findAll().stream()
                .map(emergencyBedMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmergencyBedResponse updateStatus(Long id, String status) {
        EmergencyBed bed = emergencyBedRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency bed not found with id: " + id));
        bed.setStatus(status);
        return emergencyBedMapper.toResponse(emergencyBedRepository.save(bed));
    }

    @Override
    @Transactional
    public EmergencyBedResponse assignBed(Long bedId, Long emergencyPatientId) {
        EmergencyBed bed = emergencyBedRepository.findById(bedId)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency bed not found with id: " + bedId));
        EmergencyPatient patient = emergencyPatientRepository.findById(emergencyPatientId)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency patient not found with id: " + emergencyPatientId));

        bed.setStatus("OCCUPIED");
        bed.setEmergencyPatient(patient);
        bed.setAssignedAt(LocalDateTime.now());

        return emergencyBedMapper.toResponse(emergencyBedRepository.save(bed));
    }

    @Override
    @Transactional
    public EmergencyBedResponse releaseBed(Long bedId) {
        EmergencyBed bed = emergencyBedRepository.findById(bedId)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency bed not found with id: " + bedId));

        bed.setStatus("AVAILABLE");
        bed.setEmergencyPatient(null);
        bed.setReleasedAt(LocalDateTime.now());

        return emergencyBedMapper.toResponse(emergencyBedRepository.save(bed));
    }

    @Override
    public List<EmergencyBedResponse> getByStatus(String status) {
        return emergencyBedRepository.findByStatus(status).stream()
                .map(emergencyBedMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmergencyBedResponse> getByWardName(String wardName) {
        return emergencyBedRepository.findByWardName(wardName).stream()
                .map(emergencyBedMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Long getAvailableCount() {
        return emergencyBedRepository.countByStatus("AVAILABLE");
    }

    @Override
    public Map<String, Object> getWardSummary() {
        Map<String, Object> summary = new LinkedHashMap<>();
        List<Object[]> wardData = emergencyBedRepository.getWardSummary();

        for (Object[] row : wardData) {
            String wardName = (String) row[0];
            String status = (String) row[1];
            Long count = (Long) row[2];

            if (!summary.containsKey(wardName)) {
                Map<String, Object> wardInfo = new LinkedHashMap<>();
                wardInfo.put("total", 0L);
                wardInfo.put("available", 0L);
                wardInfo.put("occupied", 0L);
                summary.put(wardName, wardInfo);
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> wardInfo = (Map<String, Object>) summary.get(wardName);
            Long currentTotal = (Long) wardInfo.get("total");
            wardInfo.put("total", currentTotal + count);

            if ("AVAILABLE".equals(status)) {
                wardInfo.put("available", count);
            } else if ("OCCUPIED".equals(status)) {
                wardInfo.put("occupied", count);
            }
        }

        return summary;
    }
}
