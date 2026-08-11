package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyLabOrderRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyLabOrderResponse;
import emranhss.com.Modern_Hospital_Management_System.service.EmergencyLabOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emergency/lab-orders")
@CrossOrigin("*")
@RequiredArgsConstructor
public class EmergencyLabOrderController {

    private final EmergencyLabOrderService emergencyLabOrderService;

    @PostMapping
    public ResponseEntity<EmergencyLabOrderResponse> create(@RequestBody EmergencyLabOrderRequest request) {
        return new ResponseEntity<>(emergencyLabOrderService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmergencyLabOrderResponse>> getAll() {
        return ResponseEntity.ok(emergencyLabOrderService.getAll());
    }

    @GetMapping("/patient/{emergencyPatientId}")
    public ResponseEntity<List<EmergencyLabOrderResponse>> getByEmergencyPatientId(
            @PathVariable Long emergencyPatientId) {
        return ResponseEntity.ok(emergencyLabOrderService.getByEmergencyPatientId(emergencyPatientId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<EmergencyLabOrderResponse> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(emergencyLabOrderService.updateStatus(id, body.get("status")));
    }

    @PutMapping("/{id}/result")
    public ResponseEntity<EmergencyLabOrderResponse> updateResult(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(emergencyLabOrderService.updateResult(id, body.get("result")));
    }

    @GetMapping("/critical")
    public ResponseEntity<List<EmergencyLabOrderResponse>> getCriticalOrders() {
        return ResponseEntity.ok(emergencyLabOrderService.getCriticalOrders());
    }
}
