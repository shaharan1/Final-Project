package emranhss.com.Modern_Hospital_Management_System.controller;


import emranhss.com.Modern_Hospital_Management_System.dto.request.AdmitPatientInvoiceRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.AdmitPatientInvoiceResponse;
import emranhss.com.Modern_Hospital_Management_System.service.AdmitPatientInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices/admitted-patients")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AdmitPatientInvoiceController {

    private final AdmitPatientInvoiceService invoiceService;



    @GetMapping("/draft/{admittedPatientId}")
    public ResponseEntity<AdmitPatientInvoiceResponse> getDraftInvoice(@PathVariable Long admittedPatientId) {
        return ResponseEntity.ok(invoiceService.calculateDraftInvoice(admittedPatientId));
    }

    @PostMapping("/finalize")
    public ResponseEntity<AdmitPatientInvoiceResponse> finalizeInvoice(@RequestBody AdmitPatientInvoiceRequest request) {
        return new ResponseEntity<>(invoiceService.finalizeAndSaveInvoice(request), HttpStatus.CREATED);
    }

}
