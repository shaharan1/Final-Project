package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.entity.Dietician;
import emranhss.com.Modern_Hospital_Management_System.service.DieticianService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/dieticians")
@CrossOrigin("*")
@RequiredArgsConstructor
public class DieticianController {

    private final DieticianService dieticianService;

    @PostMapping
    public ResponseEntity<Dietician> create(@RequestBody Dietician dietician) {
        return new ResponseEntity<>(dieticianService.create(dietician), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dietician> getById(@PathVariable Long id) {
        return ResponseEntity.ok(dieticianService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Dietician>> getAll() {
        return ResponseEntity.ok(dieticianService.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Dietician>> getActive() {
        return ResponseEntity.ok(dieticianService.getActive());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Dietician>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(dieticianService.search(keyword));
    }

    @GetMapping("/count/active")
    public ResponseEntity<Long> getActiveCount() {
        return ResponseEntity.ok(dieticianService.getActiveCount());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dietician> update(@PathVariable Long id, @RequestBody Dietician dietician) {
        return ResponseEntity.ok(dieticianService.update(id, dietician));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dieticianService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
