package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.response.TestOrderResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Tests;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.TestsRepository;
import emranhss.com.Modern_Hospital_Management_System.service.TestsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
    public TestOrderResponse collectSample(Long id, String collectorName, String sampleType) {
        Tests test = testsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test order not found with ID: " + id));
        test.setOrderStatus("SAMPLE_COLLECTED");
        test.setSampleCollectorName(collectorName);
        test.setSampleType(sampleType);
        test.setSampleCollectedDate(LocalDateTime.now());
        return toResponse(testsRepository.save(test));
    }

    @Override
    @Transactional
    public TestOrderResponse receiveSample(Long id, String receivedBy) {
        Tests test = testsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test order not found with ID: " + id));
        test.setOrderStatus("SAMPLE_RECEIVED");
        test.setSampleReceivedDate(LocalDateTime.now());
        test.setSampleReceivedBy(receivedBy);
        return toResponse(testsRepository.save(test));
    }

    @Override
    @Transactional
    public TestOrderResponse startTesting(Long id) {
        Tests test = testsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test order not found with ID: " + id));
        test.setOrderStatus("TESTING");
        test.setTestingStartDate(LocalDateTime.now());
        return toResponse(testsRepository.save(test));
    }

    @Override
    @Transactional
    public TestOrderResponse enterResult(Long id, String resultValue, String resultNotes, String enteredBy) {
        Tests test = testsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test order not found with ID: " + id));
        test.setOrderStatus("RESULT_ENTERED");
        test.setResultValue(resultValue);
        test.setResultNotes(resultNotes);
        test.setResultEnteredBy(enteredBy);
        test.setResultEnteredDate(LocalDateTime.now());
        return toResponse(testsRepository.save(test));
    }

    @Override
    @Transactional
    public TestOrderResponse verifyResult(Long id, String verifiedBy, String verificationNotes) {
        Tests test = testsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test order not found with ID: " + id));
        test.setOrderStatus("VERIFIED");
        test.setVerifiedBy(verifiedBy);
        test.setVerifiedDate(LocalDateTime.now());
        test.setVerificationNotes(verificationNotes);
        return toResponse(testsRepository.save(test));
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(String status) {
        return testsRepository.countByOrderStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getStats() {
        long pending = testsRepository.countByOrderStatus("PENDING");
        long sampleCollected = testsRepository.countByOrderStatus("SAMPLE_COLLECTED");
        long sampleReceived = testsRepository.countByOrderStatus("SAMPLE_RECEIVED");
        long testing = testsRepository.countByOrderStatus("TESTING");
        long resultEntered = testsRepository.countByOrderStatus("RESULT_ENTERED");
        long verified = testsRepository.countByOrderStatus("VERIFIED");
        long completed = testsRepository.countByOrderStatus("COMPLETED");
        return Map.of(
                "pending", pending,
                "sampleCollected", sampleCollected,
                "sampleReceived", sampleReceived,
                "testing", testing,
                "resultEntered", resultEntered,
                "verified", verified,
                "completed", completed,
                "total", pending + sampleCollected + sampleReceived + testing + resultEntered + verified + completed
        );
    }

    private TestOrderResponse toResponse(Tests test) {
        TestOrderResponse resp = new TestOrderResponse();
        resp.setId(test.getId());
        resp.setOrderStatus(test.getOrderStatus());
        resp.setOrderedDate(test.getOrderedDate());
        resp.setLastUpdated(test.getLastUpdated());

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
            resp.setPatientPhone(test.getPatient().getPhone());
            resp.setPatientGender(test.getPatient().getGender());
        }

        if (test.getPrescribedBy() != null) {
            resp.setDoctorId(test.getPrescribedBy().getId());
            resp.setDoctorName(test.getPrescribedBy().getUser().getName());
            resp.setDoctorSpecialization(test.getPrescribedBy().getSpecialization());
        }

        if (test.getPrescription() != null) {
            resp.setPrescriptionId(test.getPrescription().getId());
        }

        resp.setSampleCollectorName(test.getSampleCollectorName());
        resp.setSampleType(test.getSampleType());
        resp.setSampleCollectedDate(test.getSampleCollectedDate());
        resp.setSampleReceivedDate(test.getSampleReceivedDate());
        resp.setSampleReceivedBy(test.getSampleReceivedBy());
        resp.setTestingStartDate(test.getTestingStartDate());
        resp.setResultValue(test.getResultValue());
        resp.setResultNotes(test.getResultNotes());
        resp.setResultEnteredDate(test.getResultEnteredDate());
        resp.setResultEnteredBy(test.getResultEnteredBy());
        resp.setVerifiedBy(test.getVerifiedBy());
        resp.setVerifiedDate(test.getVerifiedDate());
        resp.setVerificationNotes(test.getVerificationNotes());

        return resp;
    }
}
