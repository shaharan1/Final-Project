package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.MedicineStockRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.StockAdjustmentRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.MedicineStockResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.StockHistoryResponse;
import emranhss.com.Modern_Hospital_Management_System.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@CrossOrigin("*")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping
    public ResponseEntity<MedicineStockResponse> addStock(@RequestBody MedicineStockRequest request) {
        return new ResponseEntity<>(stockService.addStock(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineStockResponse> updateStock(@PathVariable Long id, @RequestBody MedicineStockRequest request) {
        return ResponseEntity.ok(stockService.updateStock(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineStockResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<MedicineStockResponse>> getAll() {
        return ResponseEntity.ok(stockService.getAll());
    }

    @GetMapping("/available")
    public ResponseEntity<List<MedicineStockResponse>> getAvailable() {
        return ResponseEntity.ok(stockService.getAvailable());
    }

    @GetMapping("/search")
    public ResponseEntity<List<MedicineStockResponse>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(stockService.search(keyword));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<MedicineStockResponse>> getLowStock() {
        return ResponseEntity.ok(stockService.getLowStock());
    }

    @GetMapping("/expired")
    public ResponseEntity<List<MedicineStockResponse>> getExpired() {
        return ResponseEntity.ok(stockService.getExpired());
    }

    @GetMapping("/expiring-soon")
    public ResponseEntity<List<MedicineStockResponse>> getExpiringSoon(@RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(stockService.getExpiringSoon(days));
    }

    @GetMapping("/batch/{batch}")
    public ResponseEntity<List<MedicineStockResponse>> getByBatch(@PathVariable String batch) {
        return ResponseEntity.ok(stockService.getByBatch(batch));
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<MedicineStockResponse>> getBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(stockService.getBySupplier(supplierId));
    }

    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<MedicineStockResponse> getByBarcode(@PathVariable String barcode) {
        return ResponseEntity.ok(stockService.getByBarcode(barcode));
    }

    @PostMapping("/adjust")
    public ResponseEntity<MedicineStockResponse> adjustStock(@RequestBody StockAdjustmentRequest request) {
        return ResponseEntity.ok(stockService.adjustStock(request));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<StockHistoryResponse>> getStockHistory(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.getStockHistory(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        stockService.delete(id);
        return ResponseEntity.ok("Stock deleted");
    }
}
