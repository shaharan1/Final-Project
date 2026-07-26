package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.response.TestOrderResponse;
import java.util.List;

public interface TestsService {
    List<TestOrderResponse> getAllTestOrders();
    List<TestOrderResponse> getTestOrdersByStatus(String status);
    List<TestOrderResponse> getTestOrdersByPatient(Long patientId);
    List<TestOrderResponse> getTestOrdersByDoctor(Long doctorId);
    TestOrderResponse getTestOrderById(Long id);
    TestOrderResponse updateTestOrderStatus(Long id, String status);
    long countByStatus(String status);
}
