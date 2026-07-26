package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.ProcessPaymentRequest;
import emranhss.com.Modern_Hospital_Management_System.entity.Payment;
import emranhss.com.Modern_Hospital_Management_System.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/invoice/{invoiceNumber}")
    public ResponseEntity<Payment> getPaymentByInvoiceNumber(@PathVariable String invoiceNumber) {
        return ResponseEntity.ok(paymentService.getPaymentByInvoiceNumber(invoiceNumber));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Payment>> getPaymentsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(paymentService.getPaymentsByPatientId(patientId));
    }

    @PostMapping
    public ResponseEntity<Payment> processPayment(@RequestBody ProcessPaymentRequest request) {
        return new ResponseEntity<>(paymentService.processPayment(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @RequestBody ProcessPaymentRequest request) {
        return ResponseEntity.ok(paymentService.updatePayment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/dashboard-stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        return ResponseEntity.ok(paymentService.getDashboardStats());
    }

    @GetMapping("/daily-revenue")
    public ResponseEntity<Map<String, Object>> getDailyRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(paymentService.getDailyRevenue(date));
    }

    @GetMapping("/monthly-revenue")
    public ResponseEntity<Map<String, Object>> getMonthlyRevenue(
            @RequestParam int year, @RequestParam int month) {
        return ResponseEntity.ok(paymentService.getMonthlyRevenue(year, month));
    }

    @GetMapping("/method-breakdown")
    public ResponseEntity<Map<String, Object>> getPaymentMethodBreakdown() {
        return ResponseEntity.ok(paymentService.getPaymentMethodBreakdown());
    }
}
