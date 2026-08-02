package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.BillingInvoiceRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.BillingPaymentRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.BillingDashboardSummaryResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.BillingInvoiceResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.BillingPaymentResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.ChargeCategory;
import emranhss.com.Modern_Hospital_Management_System.repository.ChargeCategoryRepository;
import emranhss.com.Modern_Hospital_Management_System.service.BillingInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing-invoices")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BillingInvoiceController {

    private final BillingInvoiceService billingInvoiceService;
    private final ChargeCategoryRepository chargeCategoryRepository;

    @PostMapping
    public ResponseEntity<BillingInvoiceResponse> createInvoice(@RequestBody BillingInvoiceRequest request) {
        return new ResponseEntity<>(billingInvoiceService.createInvoice(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillingInvoiceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(billingInvoiceService.getById(id));
    }

    @GetMapping("/number/{invoiceNumber}")
    public ResponseEntity<BillingInvoiceResponse> getByInvoiceNumber(@PathVariable String invoiceNumber) {
        return ResponseEntity.ok(billingInvoiceService.getByInvoiceNumber(invoiceNumber));
    }

    @GetMapping
    public ResponseEntity<List<BillingInvoiceResponse>> getAllInvoices() {
        return ResponseEntity.ok(billingInvoiceService.getAllInvoices());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<BillingInvoiceResponse>> getByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(billingInvoiceService.getByPatientId(patientId));
    }

    @GetMapping("/admission/{admittedPatientId}")
    public ResponseEntity<List<BillingInvoiceResponse>> getByAdmittedPatientId(@PathVariable Long admittedPatientId) {
        return ResponseEntity.ok(billingInvoiceService.getByAdmittedPatientId(admittedPatientId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BillingInvoiceResponse>> searchInvoices(@RequestParam String search) {
        return ResponseEntity.ok(billingInvoiceService.searchInvoices(search));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillingInvoiceResponse> updateInvoice(@PathVariable Long id, @RequestBody BillingInvoiceRequest request) {
        return ResponseEntity.ok(billingInvoiceService.updateInvoice(id, request));
    }

    @PostMapping("/{invoiceId}/items")
    public ResponseEntity<BillingInvoiceResponse> addItem(
            @PathVariable Long invoiceId,
            @RequestBody BillingInvoiceRequest.BillingInvoiceItemRequest item) {
        return new ResponseEntity<>(billingInvoiceService.addItem(invoiceId, item), HttpStatus.CREATED);
    }

    @DeleteMapping("/{invoiceId}/items/{itemId}")
    public ResponseEntity<BillingInvoiceResponse> removeItem(@PathVariable Long invoiceId, @PathVariable Long itemId) {
        return ResponseEntity.ok(billingInvoiceService.removeItem(invoiceId, itemId));
    }

    @PutMapping("/{id}/finalize")
    public ResponseEntity<BillingInvoiceResponse> finalizeInvoice(
            @PathVariable Long id,
            @RequestParam(required = false) String finalizedBy) {
        return ResponseEntity.ok(billingInvoiceService.finalizeInvoice(id, finalizedBy));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BillingInvoiceResponse> cancelInvoice(@PathVariable Long id) {
        return ResponseEntity.ok(billingInvoiceService.cancelInvoice(id));
    }

    @PostMapping("/sync/{admittedPatientId}")
    public ResponseEntity<BillingInvoiceResponse> syncFromModules(@PathVariable Long admittedPatientId) {
        return ResponseEntity.ok(billingInvoiceService.syncFromModules(admittedPatientId));
    }

    @PostMapping("/payments")
    public ResponseEntity<BillingPaymentResponse> processPayment(@RequestBody BillingPaymentRequest request) {
        return new ResponseEntity<>(billingInvoiceService.processPayment(request), HttpStatus.CREATED);
    }

    @GetMapping("/{invoiceId}/payments")
    public ResponseEntity<List<BillingPaymentResponse>> getPaymentsByInvoice(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(billingInvoiceService.getPaymentsByInvoiceId(invoiceId));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<ChargeCategory>> getChargeCategories() {
        return ResponseEntity.ok(chargeCategoryRepository.findByActiveTrueOrderBySortOrder());
    }

    @GetMapping("/dashboard-summary")
    public ResponseEntity<BillingDashboardSummaryResponse> getDashboardSummary() {
        return ResponseEntity.ok(billingInvoiceService.getDashboardSummary());
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long id) throws Exception {
        byte[] pdf = billingInvoiceService.generatePdf(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Invoice-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
