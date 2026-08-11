package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyMedicineRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyMedicineResponse;
import emranhss.com.Modern_Hospital_Management_System.service.EmergencyMedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emergency/medicines")
@CrossOrigin("*")
@RequiredArgsConstructor
public class EmergencyMedicineController {

    private final EmergencyMedicineService emergencyMedicineService;

    @PostMapping
    public ResponseEntity<EmergencyMedicineResponse> create(@RequestBody EmergencyMedicineRequest request) {
        return new ResponseEntity<>(emergencyMedicineService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmergencyMedicineResponse>> getAll() {
        return ResponseEntity.ok(emergencyMedicineService.getAll());
    }

    @GetMapping("/patient/{emergencyPatientId}")
    public ResponseEntity<List<EmergencyMedicineResponse>> getByEmergencyPatientId(
            @PathVariable Long emergencyPatientId) {
        return ResponseEntity.ok(emergencyMedicineService.getByEmergencyPatientId(emergencyPatientId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<EmergencyMedicineResponse> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(emergencyMedicineService.updateStatus(id, body.get("status")));
    }

    @PutMapping("/{id}/request-pharmacy")
    public ResponseEntity<EmergencyMedicineResponse> requestPharmacy(@PathVariable Long id) {
        return ResponseEntity.ok(emergencyMedicineService.requestPharmacy(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmergencyMedicineResponse>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(emergencyMedicineService.getByStatus(status));
    }
}
