package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.AppointmentRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.AppointmentResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Appointment;
import emranhss.com.Modern_Hospital_Management_System.repository.AppointmentRepository;
import emranhss.com.Modern_Hospital_Management_System.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentRepository appointmentRepository;

    // 1. Create/Book a new Appointment
    @PostMapping
    public ResponseEntity<AppointmentResponse> bookAppointment(@RequestBody AppointmentRequest request) {
        return new ResponseEntity<>(appointmentService.bookAppointment(request), HttpStatus.CREATED);
    }

    // 2. Cancel an Existing Appointment
    @PutMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponse> cancelAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(id));
    }

    // 3. View Hospital Schedule for a Specific Date
    // URL Example: http://localhost:8085/api/appointments/schedule?date=2026-07-04
    @GetMapping("/schedule")
    public ResponseEntity<List<AppointmentResponse>> viewSchedule(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(appointmentService.viewSchedule(date));
    }

    // 4. Get All Appointments Specific to a Doctor ID
    // URL Example: http://localhost:8085/api/appointments/doctor/1
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponse>> getDoctorWiseAppointments(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getDoctorWiseAppointments(doctorId));
    }

    // 5. Check if a Patient Phone number is Returning or New
    // URL Example: http://localhost:8085/api/appointments/check-returning?phone=+8801712345678
    @GetMapping("/check-returning")
    public ResponseEntity<Boolean> isReturningPatient(@RequestParam String phone) {
        return ResponseEntity.ok(appointmentService.isReturningPatient(phone));
    }

    // 6. Calculate Dynamic Fee for a Patient based on Visit Frequency
    // URL Example: http://localhost:8085/api/appointments/calculate-fee?phone=+8801712345678&doctorId=1
    @GetMapping("/calculate-fee")
    public ResponseEntity<Double> calculateFeeForPhone(
            @RequestParam String phone,
            @RequestParam Long doctorId) {
        return ResponseEntity.ok(appointmentService.calculateFeeForPhone(phone, doctorId));
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

//    --------------Filter Appointments---------------
@GetMapping("/filter")
public ResponseEntity<List<AppointmentResponse>> filterAppointments(

        @RequestParam(required = false) Long doctorId,

        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate date

) {

    return ResponseEntity.ok(
            appointmentService.filterAppointments(doctorId, date)
    );

}

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable Long id) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentById(id)
        );

    }


    @GetMapping("/number/{appointmentNumber}")
    public ResponseEntity<AppointmentResponse> getByAppointmentNumber(
            @PathVariable String appointmentNumber) {

        return ResponseEntity.ok(
                appointmentService.getByAppointmentNumber(appointmentNumber)
        );
    }
}
