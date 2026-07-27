package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.entity.DietAssignment;
import emranhss.com.Modern_Hospital_Management_System.service.DietAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/diet-assignments")
@CrossOrigin("*")
@RequiredArgsConstructor
public class DietAssignmentController {

    private final DietAssignmentService dietAssignmentService;

    @PostMapping
    public ResponseEntity<DietAssignment> create(@RequestBody DietAssignment assignment) {
        return new ResponseEntity<>(dietAssignmentService.create(assignment), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DietAssignment> getById(@PathVariable Long id) {
        return ResponseEntity.ok(dietAssignmentService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<DietAssignment>> getAll() {
        return ResponseEntity.ok(dietAssignmentService.getAll());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<DietAssignment>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(dietAssignmentService.getByStatus(status));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<DietAssignment>> getByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(dietAssignmentService.getByPatientId(patientId));
    }

    @GetMapping("/admitted-patient/{admittedPatientId}")
    public ResponseEntity<List<DietAssignment>> getByAdmittedPatientId(@PathVariable Long admittedPatientId) {
        return ResponseEntity.ok(dietAssignmentService.getByAdmittedPatientId(admittedPatientId));
    }

    @GetMapping("/dietician/{dieticianId}")
    public ResponseEntity<List<DietAssignment>> getByDieticianId(@PathVariable Long dieticianId) {
        return ResponseEntity.ok(dietAssignmentService.getByDieticianId(dieticianId));
    }

    @GetMapping("/active")
    public ResponseEntity<List<DietAssignment>> getActiveAssignments() {
        return ResponseEntity.ok(dietAssignmentService.getActiveAssignments());
    }

    @GetMapping("/count/active")
    public ResponseEntity<Long> getActiveCount() {
        return ResponseEntity.ok(dietAssignmentService.getActiveCount());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DietAssignment> update(@PathVariable Long id, @RequestBody DietAssignment assignment) {
        return ResponseEntity.ok(dietAssignmentService.update(id, assignment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dietAssignmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
