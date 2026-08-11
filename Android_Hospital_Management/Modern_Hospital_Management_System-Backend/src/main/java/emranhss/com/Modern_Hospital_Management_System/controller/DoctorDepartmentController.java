package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.DoctorDepartmentRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.DoctorDepartmentResponse;
import emranhss.com.Modern_Hospital_Management_System.service.DoctorDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/doctor-departments")
@CrossOrigin("*")
@RequiredArgsConstructor
public class DoctorDepartmentController {

    private final DoctorDepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DoctorDepartmentResponse> createDepartment(@RequestBody DoctorDepartmentRequest request) {
        return new ResponseEntity<>(departmentService.createDepartment(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDepartmentResponse> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @GetMapping
    public ResponseEntity<List<DoctorDepartmentResponse>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDepartmentResponse> updateDepartment(@PathVariable Long id, @RequestBody DoctorDepartmentRequest request) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
