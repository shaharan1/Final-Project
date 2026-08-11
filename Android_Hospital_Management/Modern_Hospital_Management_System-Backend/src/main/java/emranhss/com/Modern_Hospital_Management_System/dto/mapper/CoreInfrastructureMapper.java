package emranhss.com.Modern_Hospital_Management_System.dto.mapper;


import emranhss.com.Modern_Hospital_Management_System.dto.response.BedResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.DepartmentResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.FacilityResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.WardResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Bed;
import emranhss.com.Modern_Hospital_Management_System.entity.Department;
import emranhss.com.Modern_Hospital_Management_System.entity.Facility;
import emranhss.com.Modern_Hospital_Management_System.entity.Ward;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CoreInfrastructureMapper {



    public DepartmentResponse toResponse(Department dept) {
        if (dept == null) return null;
        DepartmentResponse resp = new DepartmentResponse();
        BeanUtils.copyProperties(dept, resp);
        return resp;
    }

    public FacilityResponse toResponse(Facility fac) {
        if (fac == null) return null;
        FacilityResponse resp = new FacilityResponse();
        BeanUtils.copyProperties(fac, resp);
        return resp;
    }

    public WardResponse toResponse(Ward ward) {
        if (ward == null) return null;
        WardResponse resp = new WardResponse();
        BeanUtils.copyProperties(ward, resp);
        if (ward.getDepartment() != null) {
            resp.setDepartmentId(ward.getDepartment().getId());
            resp.setDepartmentName(ward.getDepartment().getName());
        }
        return resp;
    }

    public BedResponse toResponse(Bed bed) {
        if (bed == null) return null;
        BedResponse resp = new BedResponse();
        resp.setId(bed.getId());
        resp.setBedNumber(bed.getBedNumber());
        resp.setStatus(bed.getStatus());

        double totalCost = 0.0;
        if (bed.getWard() != null) {
            resp.setWardId(bed.getWard().getId());
            resp.setWardName(bed.getWard().getName());
            resp.setRoomType(bed.getWard().getRoomType().name());
            totalCost += bed.getWard().getBasePricePerDay();
        }

        if (bed.getFacilities() != null) {
            resp.setFacilities(bed.getFacilities().stream().map(this::toResponse).collect(Collectors.toList()));
            double facilityCosts = bed.getFacilities().stream().mapToDouble(Facility::getStandardCharge).sum();
            totalCost += facilityCosts;
        }
        resp.setTotalDailyCost(totalCost);
        return resp;
    }
}
