package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.EmergencyTimelineMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyTimelineRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyTimelineResponse;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.EmergencyPatientRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.EmergencyTimelineRepository;
import emranhss.com.Modern_Hospital_Management_System.service.EmergencyTimelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmergencyTimelineServiceImp implements EmergencyTimelineService {

    private final EmergencyTimelineRepository emergencyTimelineRepository;
    private final EmergencyTimelineMapper emergencyTimelineMapper;
    private final EmergencyPatientRepository emergencyPatientRepository;

    @Override
    public List<EmergencyTimelineResponse> getByEmergencyPatientId(Long emergencyPatientId) {
        return emergencyTimelineRepository.findByEmergencyPatientIdOrderByEventTimeAsc(emergencyPatientId).stream()
                .map(emergencyTimelineMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmergencyTimelineResponse> getAll() {
        return emergencyTimelineRepository.findAll().stream()
                .map(emergencyTimelineMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmergencyTimelineResponse addEvent(Long emergencyPatientId, EmergencyTimelineRequest request) {
        emergencyPatientRepository.findById(emergencyPatientId)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency patient not found with id: " + emergencyPatientId));

        emranhss.com.Modern_Hospital_Management_System.entity.EmergencyTimeline timeline =
                emergencyTimelineMapper.toEntity(emergencyPatientId, request.getEventType(),
                        request.getDescription(), request.getPerformedBy(), request.getDepartment());
        emranhss.com.Modern_Hospital_Management_System.entity.EmergencyTimeline saved =
                emergencyTimelineRepository.save(timeline);
        return emergencyTimelineMapper.toResponse(saved);
    }

    @Override
    public List<EmergencyTimelineResponse> getRecentEvents() {
        return emergencyTimelineRepository.findTop20ByOrderByEventTimeDesc().stream()
                .map(emergencyTimelineMapper::toResponse)
                .collect(Collectors.toList());
    }
}
