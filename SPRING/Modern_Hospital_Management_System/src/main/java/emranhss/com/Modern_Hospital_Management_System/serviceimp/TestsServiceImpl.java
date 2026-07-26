package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.response.TestOrderResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Tests;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.TestsRepository;
import emranhss.com.Modern_Hospital_Management_System.service.TestsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestsServiceImpl implements TestsService {

    private final TestsRepository testsRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TestOrderResponse> getAllTestOrders() {
        return testsRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestOrderResponse> getTestOrdersByStatus(String status) {
        return testsRepository.findByOrderStatus(status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestOrderResponse> getTestOrdersByPatient(Long patientId) {
        return testsRepository.findByPatientId(patientId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestOrderResponse> getTestOrdersByDoctor(Long doctorId) {
        return testsRepository.findByPrescribedById(doctorId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TestOrderResponse getTestOrderById(Long id) {
        Tests test = testsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test order not found with ID: " + id));
        return toResponse(test);
    }

    @Override
    @Transactional
    public TestOrderResponse updateTestOrderStatus(Long id, String status) {
        Tests test = testsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test order not found with ID: " + id));
        test.setOrderStatus(status);
        return toResponse(testsRepository.save(test));
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(String status) {
        return testsRepository.countByOrderStatus(status);
    }

    private TestOrderResponse toResponse(Tests test) {
        TestOrderResponse resp = new TestOrderResponse();
        resp.setId(test.getId());
        resp.setOrderStatus(test.getOrderStatus());
        resp.setOrderedDate(test.getOrderedDate());

        if (test.getTestMaster() != null) {
            resp.setTestCode(test.getTestMaster().getTestCode());
            resp.setTestName(test.getTestMaster().getTestName());
            resp.setStandardPrice(test.getTestMaster().getStandardPrice());
            resp.setNormalRange(test.getTestMaster().getNormalRange());
        }

        if (test.getPatient() != null) {
            resp.setPatientId(test.getPatient().getId());
            resp.setPatientName(test.getPatient().getName());
            resp.setPatientCode(test.getPatient().getPatientCode());
        }

        if (test.getPrescribedBy() != null) {
            resp.setDoctorId(test.getPrescribedBy().getId());
            resp.setDoctorName(test.getPrescribedBy().getUser().getName());
        }

        if (test.getPrescription() != null) {
            resp.setPrescriptionId(test.getPrescription().getId());
        }

        return resp;
    }
}
