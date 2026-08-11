package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.AmbulanceTripRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.AmbulanceTripResponse;
import emranhss.com.Modern_Hospital_Management_System.service.AmbulanceTripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergency/ambulance-trips")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AmbulanceTripController {

    private final AmbulanceTripService ambulanceTripService;

    @PostMapping
    public ResponseEntity<AmbulanceTripResponse> create(@RequestBody AmbulanceTripRequest request) {
        return new ResponseEntity<>(ambulanceTripService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AmbulanceTripResponse>> getAll() {
        return ResponseEntity.ok(ambulanceTripService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmbulanceTripResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ambulanceTripService.getById(id));
    }

    @PutMapping("/{id}/dispatch")
    public ResponseEntity<AmbulanceTripResponse> dispatchTrip(@PathVariable Long id) {
        return ResponseEntity.ok(ambulanceTripService.dispatchTrip(id));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<AmbulanceTripResponse> completeTrip(@PathVariable Long id) {
        return ResponseEntity.ok(ambulanceTripService.completeTrip(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<AmbulanceTripResponse> cancelTrip(@PathVariable Long id) {
        return ResponseEntity.ok(ambulanceTripService.cancelTrip(id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<AmbulanceTripResponse>> getActiveTrips() {
        return ResponseEntity.ok(ambulanceTripService.getActiveTrips());
    }
}
