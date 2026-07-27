package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyPatientRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyStatusUpdateRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyDashboardResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyPatientResponse;
import emranhss.com.Modern_Hospital_Management_System.service.EmergencyPatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergency/patients")
@CrossOrigin("*")
@RequiredArgsConstructor
public class EmergencyPatientController {

    private final EmergencyPatientService emergencyPatientService;

    @PostMapping
    public ResponseEntity<EmergencyPatientResponse> create(@RequestBody EmergencyPatientRequest request) {
        return new ResponseEntity<>(emergencyPatientService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmergencyPatientResponse>> getAll() {
        return ResponseEntity.ok(emergencyPatientService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyPatientResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(emergencyPatientService.getById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<EmergencyPatientResponse>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(emergencyPatientService.search(keyword));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmergencyPatientResponse>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(emergencyPatientService.getByStatus(status));
    }

    @GetMapping("/triage/{level}")
    public ResponseEntity<List<EmergencyPatientResponse>> getByTriageLevel(@PathVariable Integer level) {
        return ResponseEntity.ok(emergencyPatientService.getByTriageLevel(level));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<EmergencyDashboardResponse> getDashboard() {
        return ResponseEntity.ok(emergencyPatientService.getDashboard());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmergencyPatientResponse> update(@PathVariable Long id,
                                                            @RequestBody EmergencyPatientRequest request) {
        return ResponseEntity.ok(emergencyPatientService.update(id, request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<EmergencyPatientResponse> updateStatus(@PathVariable Long id,
                                                                   @RequestBody EmergencyStatusUpdateRequest request) {
        return ResponseEntity.ok(emergencyPatientService.updateStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        emergencyPatientService.delete(id);
        return ResponseEntity.ok().build();
    }
}
