package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.entity.InsuranceClaim;
import emranhss.com.Modern_Hospital_Management_System.service.InsuranceClaimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/insurance-claims")
@CrossOrigin("*")
@RequiredArgsConstructor
public class InsuranceClaimController {

    private final InsuranceClaimService insuranceClaimService;

    @GetMapping
    public ResponseEntity<List<InsuranceClaim>> getAll() {
        return ResponseEntity.ok(insuranceClaimService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsuranceClaim> getById(@PathVariable Long id) {
        return ResponseEntity.ok(insuranceClaimService.getById(id));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<InsuranceClaim>> getPending() {
        return ResponseEntity.ok(insuranceClaimService.getPending());
    }

    @PostMapping
    public ResponseEntity<InsuranceClaim> createClaim(@RequestBody InsuranceClaim claim) {
        return new ResponseEntity<>(insuranceClaimService.createClaim(claim), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<InsuranceClaim> approveClaim(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Double approvedAmount = ((Number) body.get("approvedAmount")).doubleValue();
        return ResponseEntity.ok(insuranceClaimService.approveClaim(id, approvedAmount));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<InsuranceClaim> rejectClaim(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(insuranceClaimService.rejectClaim(id, body.get("reason")));
    }

    @PutMapping("/{id}/settle")
    public ResponseEntity<InsuranceClaim> settleClaim(@PathVariable Long id) {
        return ResponseEntity.ok(insuranceClaimService.settleClaim(id));
    }

    @GetMapping("/insurance/{insuranceId}")
    public ResponseEntity<List<InsuranceClaim>> getByInsuranceId(@PathVariable Long insuranceId) {
        return ResponseEntity.ok(insuranceClaimService.getByInsuranceId(insuranceId));
    }
}
