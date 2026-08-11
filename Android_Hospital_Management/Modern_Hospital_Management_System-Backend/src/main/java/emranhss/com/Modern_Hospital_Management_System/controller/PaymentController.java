package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.ProcessPaymentRequest;
import emranhss.com.Modern_Hospital_Management_System.entity.Payment;
import emranhss.com.Modern_Hospital_Management_System.enums.PaymentMethod;
import emranhss.com.Modern_Hospital_Management_System.enums.PaymentStatus;
import emranhss.com.Modern_Hospital_Management_System.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<Payment>> getAll() {
        return ResponseEntity.ok(paymentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getById(id));
    }

    @GetMapping("/invoice/{invoiceNumber}")
    public ResponseEntity<List<Payment>> getByInvoiceNumber(@PathVariable String invoiceNumber) {
        return ResponseEntity.ok(paymentService.getByInvoiceNumber(invoiceNumber));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Payment>> getByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(paymentService.getByPatientId(patientId));
    }

    @PostMapping
    public ResponseEntity<Payment> processPayment(@RequestBody ProcessPaymentRequest request) {
        Payment payment = new Payment();
        payment.setInvoiceNumber(request.getInvoiceNumber());
        payment.setPatientId(request.getPatientId());
        payment.setPatientName(request.getPatientName());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()));
        payment.setTransactionId(request.getTransactionId());
        payment.setNotes(request.getNotes());
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        return new ResponseEntity<>(paymentService.processPayment(payment), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @RequestBody ProcessPaymentRequest request) {
        Payment payment = paymentService.getById(id);
        if (request.getAmount() != null) payment.setAmount(request.getAmount());
        if (request.getPaymentMethod() != null) payment.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()));
        if (request.getNotes() != null) payment.setNotes(request.getNotes());
        return ResponseEntity.ok(paymentService.updatePayment(payment));
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
    public ResponseEntity<List<Map<String, Object>>> getPaymentMethodBreakdown() {
        return ResponseEntity.ok(paymentService.getPaymentMethodBreakdown());
    }

    @GetMapping("/unpaid")
    public ResponseEntity<List<Payment>> getUnpaidBills(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long patientId) {
        if (patientId != null) {
            return ResponseEntity.ok(paymentService.getUnpaidByPatientId(patientId));
        }
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(paymentService.searchUnpaid(search));
        }
        return ResponseEntity.ok(paymentService.searchUnpaid(""));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Payment> updatePaymentStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        Payment payment = paymentService.getById(id);
        payment.setPaymentStatus(PaymentStatus.valueOf(body.get("status")));
        return ResponseEntity.ok(paymentService.updatePayment(payment));
    }
}
