package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyDoctorAssignmentRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyDoctorAssignmentResponse;
import emranhss.com.Modern_Hospital_Management_System.service.EmergencyDoctorAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergency/assignments")
@CrossOrigin("*")
@RequiredArgsConstructor
public class EmergencyDoctorAssignmentController {

    private final EmergencyDoctorAssignmentService emergencyDoctorAssignmentService;

    @PostMapping
    public ResponseEntity<EmergencyDoctorAssignmentResponse> create(
            @RequestBody EmergencyDoctorAssignmentRequest request) {
        return new ResponseEntity<>(emergencyDoctorAssignmentService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmergencyDoctorAssignmentResponse>> getAll() {
        return ResponseEntity.ok(emergencyDoctorAssignmentService.getAll());
    }

    @GetMapping("/patient/{emergencyPatientId}")
    public ResponseEntity<List<EmergencyDoctorAssignmentResponse>> getByEmergencyPatientId(
            @PathVariable Long emergencyPatientId) {
        return ResponseEntity.ok(emergencyDoctorAssignmentService.getByEmergencyPatientId(emergencyPatientId));
    }

    @PutMapping("/{id}/unassign")
    public ResponseEntity<EmergencyDoctorAssignmentResponse> unassign(@PathVariable Long id) {
        return ResponseEntity.ok(emergencyDoctorAssignmentService.unassign(id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<EmergencyDoctorAssignmentResponse>> getActiveAssignments() {
        return ResponseEntity.ok(emergencyDoctorAssignmentService.getActiveAssignments());
    }
}
