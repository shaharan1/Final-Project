package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.OfficeStaffRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.OfficeStaffResponse;
import emranhss.com.Modern_Hospital_Management_System.service.OfficeStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/office-staff")
@RequiredArgsConstructor
@CrossOrigin("*")
public class OfficeStaffController {

    private final OfficeStaffService officeStaffService;

    @PostMapping("/create")
    public ResponseEntity<OfficeStaffResponse> createOfficeStaff(
            @RequestBody OfficeStaffRequest request) {

        return ResponseEntity.ok(
                officeStaffService.createOfficeStaff(request));
    }

    @GetMapping
    public ResponseEntity<List<OfficeStaffResponse>> getAllOfficeStaff() {

        return ResponseEntity.ok(
                officeStaffService.getAllOfficeStaff());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfficeStaffResponse> getOfficeStaffById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                officeStaffService.getOfficeStaffById(id));
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<OfficeStaffResponse>> getByDepartment(
            @PathVariable String department) {

        return ResponseEntity.ok(
                officeStaffService.getOfficeStaffByDepartment(department));
    }

    @GetMapping("/position/{position}")
    public ResponseEntity<List<OfficeStaffResponse>> getByPosition(
            @PathVariable String position) {

        return ResponseEntity.ok(
                officeStaffService.getOfficeStaffByPosition(position));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfficeStaffResponse> updateOfficeStaff(
            @PathVariable Long id,
            @RequestBody OfficeStaffRequest request) {

        return ResponseEntity.ok(
                officeStaffService.updateOfficeStaff(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOfficeStaff(
            @PathVariable Long id) {

        officeStaffService.deleteOfficeStaff(id);

        return ResponseEntity.ok("Office Staff Deleted Successfully");
    }

}