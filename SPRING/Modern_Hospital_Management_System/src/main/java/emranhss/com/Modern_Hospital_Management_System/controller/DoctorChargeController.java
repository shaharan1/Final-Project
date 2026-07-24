package emranhss.com.Modern_Hospital_Management_System.controller;


import emranhss.com.Modern_Hospital_Management_System.dto.request.DoctorChargeRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.DoctorChargeResponse;
import emranhss.com.Modern_Hospital_Management_System.service.DoctorChargeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor-charges")
@CrossOrigin("*")
@RequiredArgsConstructor
public class DoctorChargeController {



    private final DoctorChargeService doctorChargeService;

    @PostMapping
    public ResponseEntity<DoctorChargeResponse> create(@RequestBody DoctorChargeRequest request) {
        DoctorChargeResponse response = doctorChargeService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorChargeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorChargeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<DoctorChargeResponse>> getAll() {
        return ResponseEntity.ok(doctorChargeService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorChargeResponse> update(@PathVariable Long id,
                                                       @RequestBody DoctorChargeRequest request) {
        return ResponseEntity.ok(doctorChargeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        doctorChargeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
