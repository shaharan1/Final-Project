package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.BedRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.DepartmentRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.FacilityRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.WardRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.BedResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.DepartmentResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.FacilityResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.WardResponse;
import emranhss.com.Modern_Hospital_Management_System.enums.BedStatus;

import java.util.List;

public interface InfrastructureService {


    DepartmentResponse createDepartment(DepartmentRequest request);
    FacilityResponse createFacility(FacilityRequest request);
    WardResponse createWard(WardRequest request);
    BedResponse createBed(BedRequest request);
    List<BedResponse> getBedsByStatus(BedStatus status);
    BedResponse updateBedStatus(Long id, BedStatus status);


    // ================= NEW =================

    List<WardResponse> getAllWards();

    List<BedResponse> getAllBeds();

    List<BedResponse> getBedsByWard(Long wardId);

}
