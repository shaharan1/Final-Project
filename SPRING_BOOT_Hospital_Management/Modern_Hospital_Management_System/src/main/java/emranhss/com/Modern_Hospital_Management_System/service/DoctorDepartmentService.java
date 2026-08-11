package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.DoctorDepartmentRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.DoctorDepartmentResponse;
import java.util.List;

public interface DoctorDepartmentService {
    DoctorDepartmentResponse createDepartment(DoctorDepartmentRequest request);
    DoctorDepartmentResponse getDepartmentById(Long id);
    List<DoctorDepartmentResponse> getAllDepartments();
    DoctorDepartmentResponse updateDepartment(Long id, DoctorDepartmentRequest request);
    void deleteDepartment(Long id);
}
