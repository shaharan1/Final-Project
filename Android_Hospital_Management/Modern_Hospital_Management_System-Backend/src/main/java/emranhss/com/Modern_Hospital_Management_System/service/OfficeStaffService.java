package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.OfficeStaffRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.OfficeStaffResponse;

import java.util.List;

public interface OfficeStaffService {

    OfficeStaffResponse createOfficeStaff(OfficeStaffRequest request);

    OfficeStaffResponse getOfficeStaffById(Long id);

    List<OfficeStaffResponse> getAllOfficeStaff();

    List<OfficeStaffResponse> getOfficeStaffByDepartment(String department);

    List<OfficeStaffResponse> getOfficeStaffByPosition(String position);

    OfficeStaffResponse updateOfficeStaff(Long id, OfficeStaffRequest request);

    void deleteOfficeStaff(Long id);

}