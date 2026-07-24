package emranhss.com.Modern_Hospital_Management_System.controller;


import emranhss.com.Modern_Hospital_Management_System.dto.request.PatientRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PatientResponse;
import emranhss.com.Modern_Hospital_Management_System.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PatientController {


    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(@RequestBody PatientRequest request) {
        return new ResponseEntity<>(patientService.createPatient(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<PatientResponse> getPatientByCode(@PathVariable String code) {
        return ResponseEntity.ok(patientService.getPatientByCode(code));
    }

    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long id, @RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok("Patient record deleted successfully.");
    }
}
