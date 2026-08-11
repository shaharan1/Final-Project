package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyLabOrderRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyLabOrderResponse;

import java.util.List;

public interface EmergencyLabOrderService {

    EmergencyLabOrderResponse create(EmergencyLabOrderRequest request);

    List<EmergencyLabOrderResponse> getByEmergencyPatientId(Long emergencyPatientId);

    List<EmergencyLabOrderResponse> getAll();

    EmergencyLabOrderResponse updateStatus(Long id, String status);

    EmergencyLabOrderResponse updateResult(Long id, String result);

    List<EmergencyLabOrderResponse> getByStatus(String status);

    List<EmergencyLabOrderResponse> getCriticalOrders();
}
