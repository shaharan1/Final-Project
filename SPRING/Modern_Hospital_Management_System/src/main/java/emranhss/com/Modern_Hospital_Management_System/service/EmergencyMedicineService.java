package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyMedicineRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyMedicineResponse;

import java.util.List;

public interface EmergencyMedicineService {

    EmergencyMedicineResponse create(EmergencyMedicineRequest request);

    List<EmergencyMedicineResponse> getByEmergencyPatientId(Long emergencyPatientId);

    List<EmergencyMedicineResponse> getAll();

    EmergencyMedicineResponse updateStatus(Long id, String status);

    EmergencyMedicineResponse requestPharmacy(Long id);

    List<EmergencyMedicineResponse> getByStatus(String status);
}
