package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.AmbulanceRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.AmbulanceResponse;
import emranhss.com.Modern_Hospital_Management_System.service.AmbulanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emergency/ambulances")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AmbulanceController {

    private final AmbulanceService ambulanceService;

    @PostMapping
    public ResponseEntity<AmbulanceResponse> create(@RequestBody AmbulanceRequest request) {
        return new ResponseEntity<>(ambulanceService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AmbulanceResponse>> getAll() {
        return ResponseEntity.ok(ambulanceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmbulanceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ambulanceService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AmbulanceResponse> update(@PathVariable Long id, @RequestBody AmbulanceRequest request) {
        return ResponseEntity.ok(ambulanceService.update(id, request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AmbulanceResponse> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(ambulanceService.updateStatus(id, body.get("status")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ambulanceService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AmbulanceResponse>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(ambulanceService.getByStatus(status));
    }

    @GetMapping("/available-count")
    public ResponseEntity<Long> getAvailableCount() {
        return ResponseEntity.ok(ambulanceService.getAvailableCount());
    }
}
