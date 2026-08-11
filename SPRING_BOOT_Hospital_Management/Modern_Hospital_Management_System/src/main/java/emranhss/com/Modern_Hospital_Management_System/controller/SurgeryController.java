package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.SurgeryRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SurgeryResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SurgeryScheduleResponse;
import emranhss.com.Modern_Hospital_Management_System.service.SurgeryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/surgeries")
@CrossOrigin("*")
@RequiredArgsConstructor
public class SurgeryController {

    private final SurgeryService surgeryService;

    @PostMapping
    public ResponseEntity<SurgeryResponse> create(@RequestBody SurgeryRequest request) {
        return new ResponseEntity<>(surgeryService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurgeryResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(surgeryService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<SurgeryResponse>> getAll() {
        return ResponseEntity.ok(surgeryService.getAll());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<SurgeryResponse>> getByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(surgeryService.getByPatientId(patientId));
    }

    @GetMapping("/admission/{admittedPatientId}")
    public ResponseEntity<List<SurgeryResponse>> getByAdmittedPatientId(@PathVariable Long admittedPatientId) {
        return ResponseEntity.ok(surgeryService.getByAdmittedPatientId(admittedPatientId));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<SurgeryResponse>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(surgeryService.getByDateRange(from, to));
    }

    @GetMapping("/theatre")
    public ResponseEntity<List<SurgeryResponse>> getByOperationTheatreAndDate(
            @RequestParam Long operationTheatreId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(surgeryService.getByOperationTheatreAndDate(operationTheatreId, date));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SurgeryResponse>> search(@RequestParam String q) {
        return ResponseEntity.ok(surgeryService.search(q));
    }

    @GetMapping("/schedule")
    public ResponseEntity<List<SurgeryScheduleResponse>> getSchedule(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(surgeryService.getSchedule(date));
    }

    @GetMapping("/schedule/upcoming")
    public ResponseEntity<List<SurgeryScheduleResponse>> getUpcomingSchedule() {
        return ResponseEntity.ok(surgeryService.getUpcomingSchedule());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurgeryResponse> update(@PathVariable Long id, @RequestBody SurgeryRequest request) {
        return ResponseEntity.ok(surgeryService.update(id, request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<SurgeryResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String cancellationReason) {
        return ResponseEntity.ok(surgeryService.updateStatus(id, status, cancellationReason));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        surgeryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
