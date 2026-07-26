package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.RefundRequest;
import emranhss.com.Modern_Hospital_Management_System.entity.Refund;
import emranhss.com.Modern_Hospital_Management_System.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/refunds")
@CrossOrigin("*")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @GetMapping
    public ResponseEntity<List<Refund>> getAll() {
        return ResponseEntity.ok(refundService.getAllRefunds());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Refund> getById(@PathVariable Long id) {
        return ResponseEntity.ok(refundService.getRefundById(id));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Refund>> getPending() {
        return ResponseEntity.ok(refundService.getPendingRefunds());
    }

    @PostMapping
    public ResponseEntity<Refund> createRefund(@RequestBody RefundRequest request) {
        return new ResponseEntity<>(refundService.createRefund(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Refund> approveRefund(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(refundService.approveRefund(id, body.get("approvedBy")));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Refund> rejectRefund(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(refundService.rejectRefund(id, body.get("reason")));
    }

    @PutMapping("/{id}/process")
    public ResponseEntity<Refund> processRefund(@PathVariable Long id) {
        return ResponseEntity.ok(refundService.processRefund(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Refund>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(refundService.getRefundsByStatus(status));
    }
}
