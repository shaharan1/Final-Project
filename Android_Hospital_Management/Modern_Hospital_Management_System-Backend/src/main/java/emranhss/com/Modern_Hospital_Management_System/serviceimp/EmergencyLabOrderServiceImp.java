package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.EmergencyLabOrderMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyLabOrderRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyLabOrderResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyLabOrder;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.EmergencyLabOrderRepository;
import emranhss.com.Modern_Hospital_Management_System.service.EmergencyLabOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmergencyLabOrderServiceImp implements EmergencyLabOrderService {

    private final EmergencyLabOrderRepository emergencyLabOrderRepository;
    private final EmergencyLabOrderMapper emergencyLabOrderMapper;

    @Override
    @Transactional
    public EmergencyLabOrderResponse create(EmergencyLabOrderRequest request) {
        EmergencyLabOrder order = emergencyLabOrderMapper.toEntity(request);
        order.setStatus("PENDING");
        order.setIsCritical(false);
        EmergencyLabOrder saved = emergencyLabOrderRepository.save(order);
        return emergencyLabOrderMapper.toResponse(saved);
    }

    @Override
    public List<EmergencyLabOrderResponse> getByEmergencyPatientId(Long emergencyPatientId) {
        return emergencyLabOrderRepository.findByEmergencyPatientId(emergencyPatientId).stream()
                .map(emergencyLabOrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmergencyLabOrderResponse> getAll() {
        return emergencyLabOrderRepository.findAll().stream()
                .map(emergencyLabOrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmergencyLabOrderResponse updateStatus(Long id, String status) {
        EmergencyLabOrder order = emergencyLabOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency lab order not found with id: " + id));
        order.setStatus(status);
        if ("SAMPLE_COLLECTED".equals(status)) {
            order.setSampleCollectionTime(LocalDateTime.now());
        } else if ("COMPLETED".equals(status)) {
            order.setResultTime(LocalDateTime.now());
        }
        return emergencyLabOrderMapper.toResponse(emergencyLabOrderRepository.save(order));
    }

    @Override
    @Transactional
    public EmergencyLabOrderResponse updateResult(Long id, String result) {
        EmergencyLabOrder order = emergencyLabOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency lab order not found with id: " + id));
        order.setResultValue(result);
        order.setResultTime(LocalDateTime.now());
        order.setStatus("COMPLETED");
        return emergencyLabOrderMapper.toResponse(emergencyLabOrderRepository.save(order));
    }

    @Override
    public List<EmergencyLabOrderResponse> getByStatus(String status) {
        return emergencyLabOrderRepository.findByStatus(status).stream()
                .map(emergencyLabOrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmergencyLabOrderResponse> getCriticalOrders() {
        return emergencyLabOrderRepository.findByIsCritical(true).stream()
                .map(emergencyLabOrderMapper::toResponse)
                .collect(Collectors.toList());
    }
}
