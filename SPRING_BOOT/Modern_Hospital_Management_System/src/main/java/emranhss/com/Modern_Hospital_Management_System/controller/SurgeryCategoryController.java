package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.SurgeryCategoryRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SurgeryCategoryResponse;
import emranhss.com.Modern_Hospital_Management_System.service.SurgeryCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surgery-categories")
@CrossOrigin("*")
@RequiredArgsConstructor
public class SurgeryCategoryController {

    private final SurgeryCategoryService surgeryCategoryService;

    @PostMapping
    public ResponseEntity<SurgeryCategoryResponse> create(@RequestBody SurgeryCategoryRequest request) {
        return new ResponseEntity<>(surgeryCategoryService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurgeryCategoryResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(surgeryCategoryService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<SurgeryCategoryResponse>> getAll() {
        return ResponseEntity.ok(surgeryCategoryService.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<SurgeryCategoryResponse>> getActive() {
        return ResponseEntity.ok(surgeryCategoryService.getActive());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurgeryCategoryResponse> update(@PathVariable Long id, @RequestBody SurgeryCategoryRequest request) {
        return ResponseEntity.ok(surgeryCategoryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        surgeryCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
