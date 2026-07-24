package emranhss.com.Modern_Hospital_Management_System.controller;


import emranhss.com.Modern_Hospital_Management_System.dto.request.DoctorRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.DoctorResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Doctor;
import emranhss.com.Modern_Hospital_Management_System.entity.User;
import emranhss.com.Modern_Hospital_Management_System.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin("*")
@RequiredArgsConstructor
public class DoctorController {



    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(@RequestBody DoctorRequest request) {
        return new ResponseEntity<>(doctorService.createDoctor(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<DoctorResponse>> getDoctorsBySpecialization(@PathVariable String specialization) {
        return ResponseEntity.ok(doctorService.getDoctorsBySpecialization(specialization));
    }


    @GetMapping("/doctordepartment/{id}")
    public ResponseEntity<List<DoctorResponse>> getDoctorsByDepartmentId(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorsByDepartment(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(@PathVariable Long id, @RequestBody DoctorRequest request) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok("Doctor record deleted successfully.");
    }



    @GetMapping("/user/{id}")
    public DoctorResponse getByUserId(@PathVariable Long id) {
        return doctorService.getByUserId(id);
    }


//    @GetMapping("/myProfile")
//    public ResponseEntity<Doctor> getLoggedInDoctor(Authentication authentication){
//
//        String email = authentication.getName();
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Doctor doctor = doctorRepository.findByUserId(user.getId())
//                .orElseThrow(() -> new RuntimeException("Doctor not found"));
//
//        return ResponseEntity.ok(doctor);
//    }
}
