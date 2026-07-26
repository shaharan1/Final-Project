package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.entity.DietPlan;
import emranhss.com.Modern_Hospital_Management_System.service.DietPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/diet-plans")
@CrossOrigin("*")
@RequiredArgsConstructor
public class DietPlanController {

    private final DietPlanService dietPlanService;

    @PostMapping
    public ResponseEntity<DietPlan> create(@RequestBody DietPlan dietPlan) {
        return new ResponseEntity<>(dietPlanService.create(dietPlan), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DietPlan> getById(@PathVariable Long id) {
        return ResponseEntity.ok(dietPlanService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<DietPlan>> getAll() {
        return ResponseEntity.ok(dietPlanService.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<DietPlan>> getActive() {
        return ResponseEntity.ok(dietPlanService.getActive());
    }

    @GetMapping("/type/{dietType}")
    public ResponseEntity<List<DietPlan>> getByDietType(@PathVariable String dietType) {
        return ResponseEntity.ok(dietPlanService.getByDietType(dietType));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DietPlan>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(dietPlanService.search(keyword));
    }

    @GetMapping("/count/active")
    public ResponseEntity<Long> getActiveCount() {
        return ResponseEntity.ok(dietPlanService.getActiveCount());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DietPlan> update(@PathVariable Long id, @RequestBody DietPlan dietPlan) {
        return ResponseEntity.ok(dietPlanService.update(id, dietPlan));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dietPlanService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
