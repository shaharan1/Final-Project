package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.PurchaseItemRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PurchaseItemResponse;
import emranhss.com.Modern_Hospital_Management_System.service.PurchaseItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/purchase-items")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PurchaseItemController {

    private final PurchaseItemService itemService;

    @PostMapping
    public ResponseEntity<PurchaseItemResponse> addPurchaseItem(@RequestBody PurchaseItemRequest request) {
        return new ResponseEntity<>(itemService.addPurchaseItem(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseItemResponse> getPurchaseItemById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getPurchaseItemById(id));
    }

    @GetMapping("/purchase/{purchaseId}")
    public ResponseEntity<List<PurchaseItemResponse>> getItemsByPurchaseId(@PathVariable Long purchaseId) {
        return ResponseEntity.ok(itemService.getItemsByPurchaseId(purchaseId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseItem(@PathVariable Long id) {
        itemService.deletePurchaseItem(id);
        return ResponseEntity.noContent().build();
    }
}
