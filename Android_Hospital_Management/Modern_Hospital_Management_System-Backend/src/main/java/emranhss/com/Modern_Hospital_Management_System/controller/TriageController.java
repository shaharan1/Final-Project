package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.TriageRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.TriageResponse;
import emranhss.com.Modern_Hospital_Management_System.service.TriageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emergency/triage")
@CrossOrigin("*")
@RequiredArgsConstructor
public class TriageController {

    private final TriageService triageService;

    @PostMapping
    public ResponseEntity<TriageResponse> create(@RequestBody TriageRequest request) {
        return new ResponseEntity<>(triageService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TriageResponse>> getAll() {
        return ResponseEntity.ok(triageService.getAll());
    }

    @GetMapping("/patient/{emergencyPatientId}")
    public ResponseEntity<TriageResponse> getByEmergencyPatientId(@PathVariable Long emergencyPatientId) {
        return ResponseEntity.ok(triageService.getByEmergencyPatientId(emergencyPatientId));
    }

    @GetMapping("/distribution")
    public ResponseEntity<Map<String, Object>> getTriageDistribution() {
        return ResponseEntity.ok(triageService.getTriageDistribution());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TriageResponse> update(@PathVariable Long id, @RequestBody TriageRequest request) {
        return ResponseEntity.ok(triageService.update(id, request));
    }
}
