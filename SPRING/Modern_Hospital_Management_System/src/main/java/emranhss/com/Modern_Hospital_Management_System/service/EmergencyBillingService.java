package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyBillingRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyBillingResponse;

import java.util.List;

public interface EmergencyBillingService {

    EmergencyBillingResponse create(EmergencyBillingRequest request);

    EmergencyBillingResponse getById(Long id);

    EmergencyBillingResponse getByEmergencyPatientId(Long emergencyPatientId);

    List<EmergencyBillingResponse> getAll();

    EmergencyBillingResponse generateBill(Long emergencyPatientId);

    EmergencyBillingResponse updatePaymentStatus(Long id, String status);

    List<EmergencyBillingResponse> getByStatus(String status);

    Double getTodayRevenue();
}
