package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.SupplierRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SupplierResponse;
import emranhss.com.Modern_Hospital_Management_System.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin("*")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    public ResponseEntity<SupplierResponse> create(@RequestBody SupplierRequest request) {
        return new ResponseEntity<>(supplierService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> update(@PathVariable Long id, @RequestBody SupplierRequest request) {
        return ResponseEntity.ok(supplierService.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponse>> getAll() {
        return ResponseEntity.ok(supplierService.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<SupplierResponse>> getActive() {
        return ResponseEntity.ok(supplierService.getActive());
    }

    @GetMapping("/search")
    public ResponseEntity<List<SupplierResponse>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(supplierService.search(keyword));
    }

    @GetMapping("/due")
    public ResponseEntity<List<SupplierResponse>> getWithDue() {
        return ResponseEntity.ok(supplierService.getWithDue());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        supplierService.delete(id);
        return ResponseEntity.ok("Supplier deleted");
    }
}
