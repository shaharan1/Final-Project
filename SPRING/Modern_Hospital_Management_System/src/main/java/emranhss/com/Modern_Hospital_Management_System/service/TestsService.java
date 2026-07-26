package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.response.TestOrderResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TestsService {
    List<TestOrderResponse> getAllTestOrders();
    List<TestOrderResponse> getTestOrdersByStatus(String status);
    List<TestOrderResponse> getTestOrdersByPatient(Long patientId);
    List<TestOrderResponse> getTestOrdersByDoctor(Long doctorId);
    TestOrderResponse getTestOrderById(Long id);

    // Workflow transitions
    TestOrderResponse collectSample(Long id, String collectorName, String sampleType);
    TestOrderResponse receiveSample(Long id, String receivedBy);
    TestOrderResponse startTesting(Long id);
    TestOrderResponse enterResult(Long id, String resultValue, String resultNotes, String enteredBy);
    TestOrderResponse verifyResult(Long id, String verifiedBy, String verificationNotes);

    long countByStatus(String status);
    Map<String, Long> getStats();
}
