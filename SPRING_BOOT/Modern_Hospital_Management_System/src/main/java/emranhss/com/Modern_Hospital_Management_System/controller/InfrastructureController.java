package emranhss.com.Modern_Hospital_Management_System.controller;


import emranhss.com.Modern_Hospital_Management_System.dto.request.BedRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.DepartmentRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.FacilityRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.WardRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.BedResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.DepartmentResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.FacilityResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.WardResponse;
import emranhss.com.Modern_Hospital_Management_System.enums.BedStatus;
import emranhss.com.Modern_Hospital_Management_System.service.InfrastructureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/infrastructure")
@CrossOrigin("*")
@RequiredArgsConstructor
public class InfrastructureController {


    private final InfrastructureService infraService;

    @PostMapping("/departments")
    public ResponseEntity<DepartmentResponse> createDepartment(@RequestBody DepartmentRequest request) {
        return new ResponseEntity<>(infraService.createDepartment(request), HttpStatus.CREATED);
    }

    @PostMapping("/facilities")
    public ResponseEntity<FacilityResponse> createFacility(@RequestBody FacilityRequest request) {
        return new ResponseEntity<>(infraService.createFacility(request), HttpStatus.CREATED);
    }

    @PostMapping("/wards")
    public ResponseEntity<WardResponse> createWard(@RequestBody WardRequest request) {
        return new ResponseEntity<>(infraService.createWard(request), HttpStatus.CREATED);
    }

    @PostMapping("/beds")
    public ResponseEntity<BedResponse> createBed(@RequestBody BedRequest request) {
        return new ResponseEntity<>(infraService.createBed(request), HttpStatus.CREATED);
    }

    @GetMapping("/beds/status/{status}")
    public ResponseEntity<List<BedResponse>> getBedsByStatus(@PathVariable BedStatus status) {
        return ResponseEntity.ok(infraService.getBedsByStatus(status));
    }

    @PutMapping("/beds/{id}/status")
    public ResponseEntity<BedResponse> updateBedStatus(@PathVariable Long id, @RequestParam BedStatus status) {
        return ResponseEntity.ok(infraService.updateBedStatus(id, status));
    }

//    -------------NEW-----------

    @GetMapping("/wards")
    public ResponseEntity<List<WardResponse>> getAllWards() {
        return ResponseEntity.ok(infraService.getAllWards());
    }

    @GetMapping("/beds")
    public ResponseEntity<List<BedResponse>> getAllBeds() {
        return ResponseEntity.ok(infraService.getAllBeds());
    }

    @GetMapping("/wards/{wardId}/beds")
    public ResponseEntity<List<BedResponse>> getBedsByWard(@PathVariable Long wardId) {
        return ResponseEntity.ok(infraService.getBedsByWard(wardId));
    }

    @GetMapping("/facilities")
    public ResponseEntity<List<FacilityResponse>> getAllFacilities() {
        return ResponseEntity.ok(infraService.getAllFacilities());
    }

    @PutMapping("/beds/{id}/facilities")
    public ResponseEntity<BedResponse> updateBedFacilities(
            @PathVariable Long id,
            @RequestBody java.util.Set<Long> facilityIds) {
        return ResponseEntity.ok(infraService.updateBedFacilities(id, facilityIds));
    }

    @PutMapping("/wards/{id}")
    public ResponseEntity<WardResponse> updateWard(@PathVariable Long id, @RequestBody WardRequest request) {
        return ResponseEntity.ok(infraService.updateWard(id, request));
    }

    @DeleteMapping("/wards/{id}")
    public ResponseEntity<Void> deleteWard(@PathVariable Long id) {
        infraService.deleteWard(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/beds/{id}")
    public ResponseEntity<Void> deleteBed(@PathVariable Long id) {
        infraService.deleteBed(id);
        return ResponseEntity.noContent().build();
    }

}
