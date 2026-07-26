package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.response.TestOrderResponse;
import emranhss.com.Modern_Hospital_Management_System.service.TestsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/test-orders")
@RequiredArgsConstructor
public class TestsController {

    private final TestsService testsService;

    @GetMapping
    public ResponseEntity<List<TestOrderResponse>> getAllTestOrders() {
        return ResponseEntity.ok(testsService.getAllTestOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestOrderResponse> getTestOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(testsService.getTestOrderById(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TestOrderResponse>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(testsService.getTestOrdersByStatus(status));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<TestOrderResponse>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(testsService.getTestOrdersByPatient(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<TestOrderResponse>> getByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(testsService.getTestOrdersByDoctor(doctorId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TestOrderResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String status = body.get("status");
        return ResponseEntity.ok(testsService.updateTestOrderStatus(id, status));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        long pending = testsService.countByStatus("PENDING");
        long collected = testsService.countByStatus("SAMPLE_COLLECTED");
        long completed = testsService.countByStatus("COMPLETED");
        return ResponseEntity.ok(Map.of(
                "pending", pending,
                "sampleCollected", collected,
                "completed", completed,
                "total", pending + collected + completed
        ));
    }
}
