package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.DoctorDepartmentRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.DoctorDepartmentResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.DoctorDepartment;
import org.springframework.stereotype.Component;

@Component
public class DoctorDepartmentMapper {

    public DoctorDepartment toEntity(DoctorDepartmentRequest request) {
        if (request == null) return null;

        DoctorDepartment department = new DoctorDepartment();
        department.setDepartmentName(request.getDepartmentName());
        department.setDescription(request.getDescription());
        return department;
    }

    public DoctorDepartmentResponse toResponse(DoctorDepartment department) {
        if (department == null) return null;

        DoctorDepartmentResponse response = new DoctorDepartmentResponse();
        response.setId(department.getId());
        response.setDepartmentName(department.getDepartmentName());
        response.setDescription(department.getDescription());
        return response;
    }
}
