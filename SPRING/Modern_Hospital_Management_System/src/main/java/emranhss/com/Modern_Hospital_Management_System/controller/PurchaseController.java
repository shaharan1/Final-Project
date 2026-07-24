package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.PurchaseRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PurchaseResponse;
import emranhss.com.Modern_Hospital_Management_System.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<PurchaseResponse> createPurchase(@RequestBody PurchaseRequest request) {
        return new ResponseEntity<>(purchaseService.createPurchase(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseResponse> getPurchaseById(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseService.getPurchaseById(id));
    }

    @GetMapping
    public ResponseEntity<List<PurchaseResponse>> getAllPurchases() {
        return ResponseEntity.ok(purchaseService.getAllPurchases());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        purchaseService.deletePurchase(id);
        return ResponseEntity.noContent().build();
    }
}
