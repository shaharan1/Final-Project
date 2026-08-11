package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyBillingRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyBillingResponse;
import emranhss.com.Modern_Hospital_Management_System.service.EmergencyBillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emergency/billing")
@CrossOrigin("*")
@RequiredArgsConstructor
public class EmergencyBillingController {

    private final EmergencyBillingService emergencyBillingService;

    @PostMapping
    public ResponseEntity<EmergencyBillingResponse> create(@RequestBody EmergencyBillingRequest request) {
        return new ResponseEntity<>(emergencyBillingService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmergencyBillingResponse>> getAll() {
        return ResponseEntity.ok(emergencyBillingService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyBillingResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(emergencyBillingService.getById(id));
    }

    @GetMapping("/patient/{emergencyPatientId}")
    public ResponseEntity<EmergencyBillingResponse> getByEmergencyPatientId(
            @PathVariable Long emergencyPatientId) {
        return ResponseEntity.ok(emergencyBillingService.getByEmergencyPatientId(emergencyPatientId));
    }

    @PostMapping("/generate/{emergencyPatientId}")
    public ResponseEntity<EmergencyBillingResponse> generateBill(@PathVariable Long emergencyPatientId) {
        return new ResponseEntity<>(emergencyBillingService.generateBill(emergencyPatientId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/payment-status")
    public ResponseEntity<EmergencyBillingResponse> updatePaymentStatus(@PathVariable Long id,
                                                                         @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(emergencyBillingService.updatePaymentStatus(id, body.get("status")));
    }

    @GetMapping("/today-revenue")
    public ResponseEntity<Double> getTodayRevenue() {
        return ResponseEntity.ok(emergencyBillingService.getTodayRevenue());
    }
}
