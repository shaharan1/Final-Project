package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.entity.MealSchedule;
import emranhss.com.Modern_Hospital_Management_System.service.MealScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/meal-schedules")
@CrossOrigin("*")
@RequiredArgsConstructor
public class MealScheduleController {

    private final MealScheduleService mealScheduleService;

    @PostMapping
    public ResponseEntity<MealSchedule> create(@RequestBody MealSchedule schedule) {
        return new ResponseEntity<>(mealScheduleService.create(schedule), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealSchedule> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mealScheduleService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<MealSchedule>> getAll() {
        return ResponseEntity.ok(mealScheduleService.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<MealSchedule>> getActive() {
        return ResponseEntity.ok(mealScheduleService.getActive());
    }

    @GetMapping("/current")
    public ResponseEntity<MealSchedule> getCurrentMeal() {
        return ResponseEntity.ok(mealScheduleService.getCurrentMeal());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MealSchedule> update(@PathVariable Long id, @RequestBody MealSchedule schedule) {
        return ResponseEntity.ok(mealScheduleService.update(id, schedule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        mealScheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
