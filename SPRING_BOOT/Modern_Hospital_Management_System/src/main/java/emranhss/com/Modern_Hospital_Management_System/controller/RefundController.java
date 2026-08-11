package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.RefundRequest;
import emranhss.com.Modern_Hospital_Management_System.entity.Refund;
import emranhss.com.Modern_Hospital_Management_System.enums.RefundStatus;
import emranhss.com.Modern_Hospital_Management_System.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refunds")
@CrossOrigin("*")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @GetMapping
    public ResponseEntity<List<Refund>> getAll() {
        return ResponseEntity.ok(refundService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Refund> getById(@PathVariable Long id) {
        return ResponseEntity.ok(refundService.getById(id));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Refund>> getPending() {
        return ResponseEntity.ok(refundService.getPending());
    }

    @PostMapping
    public ResponseEntity<Refund> createRefund(@RequestBody RefundRequest request) {
        Refund refund = new Refund();
        refund.setPaymentId(request.getPaymentId());
        refund.setInvoiceNumber(request.getInvoiceNumber());
        refund.setPatientId(request.getPatientId());
        refund.setPatientName(request.getPatientName());
        refund.setRefundAmount(request.getRefundAmount());
        refund.setRefundReason(request.getRefundReason());
        refund.setRefundType(request.getRefundType());
        refund.setProcessedBy(request.getProcessedBy());
        refund.setNotes(request.getNotes());
        return new ResponseEntity<>(refundService.createRefund(refund), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Refund> approveRefund(@PathVariable Long id, @RequestBody java.util.Map<String, String> body) {
        return ResponseEntity.ok(refundService.approveRefund(id, body.get("approvedBy")));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Refund> rejectRefund(@PathVariable Long id, @RequestBody java.util.Map<String, String> body) {
        return ResponseEntity.ok(refundService.rejectRefund(id, body.get("reason")));
    }

    @PutMapping("/{id}/process")
    public ResponseEntity<Refund> processRefund(@PathVariable Long id) {
        return ResponseEntity.ok(refundService.processRefund(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Refund>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(refundService.getByStatus(RefundStatus.valueOf(status)));
    }
}
