package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.entity.PatientDietAlert;
import emranhss.com.Modern_Hospital_Management_System.service.PatientDietAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/diet-alerts")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PatientDietAlertController {

    private final PatientDietAlertService patientDietAlertService;

    @PostMapping
    public ResponseEntity<PatientDietAlert> create(@RequestBody PatientDietAlert alert) {
        return new ResponseEntity<>(patientDietAlertService.create(alert), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDietAlert> getById(@PathVariable Long id) {
        return ResponseEntity.ok(patientDietAlertService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<PatientDietAlert>> getAll() {
        return ResponseEntity.ok(patientDietAlertService.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<PatientDietAlert>> getActive() {
        return ResponseEntity.ok(patientDietAlertService.getActive());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PatientDietAlert>> getByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientDietAlertService.getByPatientId(patientId));
    }

    @GetMapping("/type/{alertType}")
    public ResponseEntity<List<PatientDietAlert>> getByAlertType(@PathVariable String alertType) {
        return ResponseEntity.ok(patientDietAlertService.getByAlertType(alertType));
    }

    @GetMapping("/count/active")
    public ResponseEntity<Long> getActiveCount() {
        return ResponseEntity.ok(patientDietAlertService.getActiveCount());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PatientDietAlert> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(patientDietAlertService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientDietAlertService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
