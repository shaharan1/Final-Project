package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyBedRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyBedResponse;
import emranhss.com.Modern_Hospital_Management_System.service.EmergencyBedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emergency/beds")
@CrossOrigin("*")
@RequiredArgsConstructor
public class EmergencyBedController {

    private final EmergencyBedService emergencyBedService;

    @PostMapping
    public ResponseEntity<EmergencyBedResponse> create(@RequestBody EmergencyBedRequest request) {
        return new ResponseEntity<>(emergencyBedService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmergencyBedResponse>> getAll() {
        return ResponseEntity.ok(emergencyBedService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyBedResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(emergencyBedService.getById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<EmergencyBedResponse> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(emergencyBedService.updateStatus(id, body.get("status")));
    }

    @PutMapping("/{bedId}/assign")
    public ResponseEntity<EmergencyBedResponse> assignBed(@PathVariable Long bedId,
                                                           @RequestBody Map<String, Long> body) {
        return ResponseEntity.ok(emergencyBedService.assignBed(bedId, body.get("emergencyPatientId")));
    }

    @PutMapping("/{id}/release")
    public ResponseEntity<EmergencyBedResponse> releaseBed(@PathVariable Long id) {
        return ResponseEntity.ok(emergencyBedService.releaseBed(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmergencyBedResponse>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(emergencyBedService.getByStatus(status));
    }

    @GetMapping("/ward/{wardName}")
    public ResponseEntity<List<EmergencyBedResponse>> getByWardName(@PathVariable String wardName) {
        return ResponseEntity.ok(emergencyBedService.getByWardName(wardName));
    }

    @GetMapping("/available-count")
    public ResponseEntity<Long> getAvailableCount() {
        return ResponseEntity.ok(emergencyBedService.getAvailableCount());
    }

    @GetMapping("/ward-summary")
    public ResponseEntity<Map<String, Object>> getWardSummary() {
        return ResponseEntity.ok(emergencyBedService.getWardSummary());
    }
}
