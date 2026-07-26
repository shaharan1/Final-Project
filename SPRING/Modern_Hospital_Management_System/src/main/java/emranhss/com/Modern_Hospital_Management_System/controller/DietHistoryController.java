package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.entity.DietHistory;
import emranhss.com.Modern_Hospital_Management_System.service.DietHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/diet-history")
@CrossOrigin("*")
@RequiredArgsConstructor
public class DietHistoryController {

    private final DietHistoryService dietHistoryService;

    @PostMapping
    public ResponseEntity<DietHistory> create(@RequestBody DietHistory history) {
        return new ResponseEntity<>(dietHistoryService.create(history), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DietHistory>> getAll() {
        return ResponseEntity.ok(dietHistoryService.getAll());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<DietHistory>> getByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(dietHistoryService.getByPatientId(patientId));
    }

    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<DietHistory>> getByDietAssignmentId(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(dietHistoryService.getByDietAssignmentId(assignmentId));
    }
}
