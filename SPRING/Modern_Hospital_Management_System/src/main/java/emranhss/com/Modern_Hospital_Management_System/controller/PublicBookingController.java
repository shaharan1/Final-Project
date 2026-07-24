package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.AppointmentRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.AppointmentResponse;
import emranhss.com.Modern_Hospital_Management_System.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/public/checkout")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PublicBookingController {

    private final AppointmentService appointmentService;

    /**
     * Frontend checks the rate dynamically when the patient enters their mobile number.
     */
    @GetMapping("/calculate-rate")
    public ResponseEntity<Map<String, Object>> liveRateCheck(@RequestParam String phone, @RequestParam Long doctorId) {
        Double calculatedFee = appointmentService.calculateFeeForPhone(phone, doctorId);
        Boolean hasVisitedBefore = appointmentService.isReturningPatient(phone);

        return ResponseEntity.ok(Map.of(
                "fee", calculatedFee,
                "status", hasVisitedBefore ? "RETURNING_PATIENT" : "NEW_PATIENT"
        ));
    }

    /**
     * Confirms booking after checking out with bKash or Cash.
     */
    @PostMapping("/confirm-booking")
    public ResponseEntity<AppointmentResponse> processPublicBooking(@RequestBody AppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.bookAppointment(request));
    }
}