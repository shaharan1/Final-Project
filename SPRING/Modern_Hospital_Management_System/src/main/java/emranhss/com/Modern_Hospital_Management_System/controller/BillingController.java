package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.response.BillingResponse;
import emranhss.com.Modern_Hospital_Management_System.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billings")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    // Triggers real-time synchronization calculation maps across underlying transaction tables
    @GetMapping("/sync-summary/{admittedPatientId}")
    public ResponseEntity<BillingResponse> getSyncedBillingSummary(@PathVariable Long admittedPatientId) {
        return ResponseEntity.ok(billingService.syncAndGetInpatientBilling(admittedPatientId));
    }
}
