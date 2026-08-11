package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.DoctorDiscountRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.DoctorDiscountResponse;
import emranhss.com.Modern_Hospital_Management_System.service.DoctorDiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor-discounts")
@CrossOrigin("*")
@RequiredArgsConstructor
public class DoctorDiscountController {

    private final DoctorDiscountService doctorDiscountService;

    @PostMapping
    public ResponseEntity<DoctorDiscountResponse> create(@RequestBody DoctorDiscountRequest request) {
        return new ResponseEntity<>(doctorDiscountService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDiscountResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorDiscountService.getById(id));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<DoctorDiscountResponse> getByDoctorId(@PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorDiscountService.getByDoctorId(doctorId));
    }

    @GetMapping
    public ResponseEntity<List<DoctorDiscountResponse>> getAll() {
        return ResponseEntity.ok(doctorDiscountService.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<DoctorDiscountResponse>> getActive() {
        return ResponseEntity.ok(doctorDiscountService.getActive());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDiscountResponse> update(@PathVariable Long id, @RequestBody DoctorDiscountRequest request) {
        return ResponseEntity.ok(doctorDiscountService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        doctorDiscountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
