package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.MealRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.MealResponse;
import emranhss.com.Modern_Hospital_Management_System.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @PostMapping
    public ResponseEntity<MealResponse> logMealConsumption(@RequestBody MealRequest request) {
        MealResponse response = mealService.logMealConsumption(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealResponse> getMealById(@PathVariable Long id) {
        return ResponseEntity.ok(mealService.getMealById(id));
    }

    @GetMapping
    public ResponseEntity<List<MealResponse>> getAllMeals() {
        return ResponseEntity.ok(mealService.getAllMeals());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MealResponse>> getMealsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(mealService.getMealsByPatient(patientId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MealResponse> updateMealLog(
            @PathVariable Long id,
            @RequestBody MealRequest request) {
        return ResponseEntity.ok(mealService.updateMealLog(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMealLog(@PathVariable Long id) {
        mealService.deleteMealLog(id);
        return ResponseEntity.noContent().build();
    }
}
