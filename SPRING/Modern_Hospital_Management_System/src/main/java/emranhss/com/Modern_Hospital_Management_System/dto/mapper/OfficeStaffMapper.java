package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.OfficeStaffRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.OfficeStaffResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.OfficeStaff;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class OfficeStaffMapper {

    public OfficeStaff toEntity(OfficeStaffRequest request) {

        if (request == null) {
            return null;
        }

        OfficeStaff officeStaff = new OfficeStaff();
        BeanUtils.copyProperties(request, officeStaff);

        return officeStaff;
    }

    public OfficeStaffResponse toResponse(OfficeStaff officeStaff) {

        if (officeStaff == null) {
            return null;
        }

        OfficeStaffResponse response = new OfficeStaffResponse();

        response.setId(officeStaff.getId());

        // User Information
        if (officeStaff.getUser() != null) {
            response.setName(officeStaff.getUser().getName());
            response.setEmail(officeStaff.getUser().getEmail());
            response.setPhone(officeStaff.getUser().getPhone());
        }

        // Office Staff Information
        response.setGender(officeStaff.getGender());
        response.setPosition(officeStaff.getPosition());
        response.setAge(officeStaff.getAge());
        response.setDepartment(officeStaff.getDepartment());
        response.setWorkingHours(officeStaff.getWorkingHours());
        response.setJoinDate(officeStaff.getJoinDate());
        response.setPhoto(officeStaff.getPhoto());

        return response;
    }

    public void updateEntityFromRequest(OfficeStaffRequest request,
                                        OfficeStaff officeStaff) {

        if (request == null || officeStaff == null) {
            return;
        }

        BeanUtils.copyProperties(request, officeStaff, "id");

    }
}