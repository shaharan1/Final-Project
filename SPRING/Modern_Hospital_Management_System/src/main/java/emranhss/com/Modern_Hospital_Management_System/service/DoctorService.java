package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.DoctorRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.DoctorResponse;
import java.util.List;

public interface DoctorService {
    DoctorResponse createDoctor(DoctorRequest request);
    DoctorResponse getDoctorById(Long id);
    List<DoctorResponse> getAllDoctors();
    List<DoctorResponse> getDoctorsBySpecialization(String specialization);
    DoctorResponse updateDoctor(Long id, DoctorRequest request);
    void deleteDoctor(Long id);

    // Filter doctors dynamically based on landing page choices
    List<DoctorResponse> getDoctorsByDepartment(Long departmentId);

    DoctorResponse getByUserId(Long id);
}
