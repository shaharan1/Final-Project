package emranhss.com.Modern_Hospital_Management_System.controller;


import emranhss.com.Modern_Hospital_Management_System.dto.request.InvoiceRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.InvoiceResponse;
import emranhss.com.Modern_Hospital_Management_System.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin("*")
@RequiredArgsConstructor
public class InvoiceController {



    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<InvoiceResponse> generateInvoice(@RequestBody InvoiceRequest request) {
        return new ResponseEntity<>(invoiceService.generateInvoice(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }
}
