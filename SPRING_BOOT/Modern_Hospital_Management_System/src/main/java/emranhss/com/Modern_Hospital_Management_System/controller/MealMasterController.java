package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.MealMasterRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.MealMasterResponse;
import emranhss.com.Modern_Hospital_Management_System.service.MealMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/meal-masters")
@RequiredArgsConstructor
public class MealMasterController {

    private final MealMasterService mealMasterService;

    @PostMapping
    public ResponseEntity<MealMasterResponse> createMealMaster(@RequestBody MealMasterRequest request) {
        MealMasterResponse response = mealMasterService.createMealMaster(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealMasterResponse> getMealMasterById(@PathVariable Long id) {
        return ResponseEntity.ok(mealMasterService.getMealMasterById(id));
    }

    @GetMapping
    public ResponseEntity<List<MealMasterResponse>> getAllMealMasters() {
        return ResponseEntity.ok(mealMasterService.getAllMealMasters());
    }

    @GetMapping("/active")
    public ResponseEntity<List<MealMasterResponse>> getActiveMealMasters() {
        return ResponseEntity.ok(mealMasterService.getActiveMealMasters());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MealMasterResponse> updateMealMaster(
            @PathVariable Long id,
            @RequestBody MealMasterRequest request) {
        return ResponseEntity.ok(mealMasterService.updateMealMaster(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMealMaster(@PathVariable Long id) {
        mealMasterService.deleteMealMaster(id);
        return ResponseEntity.noContent().build();
    }
}
