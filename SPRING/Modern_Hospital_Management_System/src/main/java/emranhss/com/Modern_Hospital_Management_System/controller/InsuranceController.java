package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.entity.Insurance;
import emranhss.com.Modern_Hospital_Management_System.service.InsuranceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insurance")
@CrossOrigin("*")
@RequiredArgsConstructor
public class InsuranceController {

    private final InsuranceService insuranceService;

    @GetMapping
    public ResponseEntity<List<Insurance>> getAll() {
        return ResponseEntity.ok(insuranceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Insurance> getById(@PathVariable Long id) {
        return ResponseEntity.ok(insuranceService.getById(id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Insurance>> getActive() {
        return ResponseEntity.ok(insuranceService.getActive());
    }

    @PostMapping
    public ResponseEntity<Insurance> create(@RequestBody Insurance insurance) {
        return new ResponseEntity<>(insuranceService.create(insurance), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Insurance> update(@PathVariable Long id, @RequestBody Insurance insurance) {
        return ResponseEntity.ok(insuranceService.update(id, insurance));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        insuranceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Insurance>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(insuranceService.search(keyword));
    }
}
