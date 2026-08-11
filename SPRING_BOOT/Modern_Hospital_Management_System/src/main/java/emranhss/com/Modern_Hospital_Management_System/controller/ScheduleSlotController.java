package emranhss.com.Modern_Hospital_Management_System.controller;


import emranhss.com.Modern_Hospital_Management_System.dto.request.ScheduleSlotRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.ScheduleSlotResponse;
import emranhss.com.Modern_Hospital_Management_System.service.ScheduleSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
    @RequestMapping("/api/schedule-slots")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ScheduleSlotController {



    private final ScheduleSlotService slotService;

    @PostMapping
    public ResponseEntity<ScheduleSlotResponse> createSlot(@RequestBody ScheduleSlotRequest request) {
        return new ResponseEntity<>(slotService.createSlot(request), HttpStatus.CREATED);
    }

    @GetMapping("/doctor/{doctorId}/available")
    public ResponseEntity<List<ScheduleSlotResponse>> getAvailableSlots(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(slotService.getAvailableSlotsByDoctor(doctorId, date));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ScheduleSlotResponse> toggleSlotBookingStatus(
            @PathVariable Long id,
            @RequestParam Boolean isBooked) {
        return ResponseEntity.ok(slotService.toggleSlotBookingStatus(id, isBooked));
    }

    // New Endpoint: Get and display all generated schedule slots across the system
    @GetMapping
    public ResponseEntity<List<ScheduleSlotResponse>> getAllSlots() {
        return ResponseEntity.ok(slotService.getAllSlots());
    }


    // Find a specific slot by doctor, date and start time
    @GetMapping("/doctor/{doctorId}/slot")
    public ResponseEntity<List<ScheduleSlotResponse>> findByDoctorDateAndStartTime(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime startTime) {

        return ResponseEntity.ok(
                slotService.findByDoctorDateAndStartTime(
                        doctorId,
                        date,
                        startTime
                )
        );
    }

    // Get only available (not booked) slots
    @GetMapping("/doctor/{doctorId}/free")
    public ResponseEntity<List<ScheduleSlotResponse>> findAvailableSlots(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        return ResponseEntity.ok(
                slotService.findAvailableSlots(
                        doctorId,
                        date
                )
        );
    }


    @GetMapping("/doctor/{doctorId}/booked")
    public ResponseEntity<List<ScheduleSlotResponse>> findBookedSlots(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        return ResponseEntity.ok(
                slotService.findBookedSlots(
                        doctorId,
                        date
                )
        );
    }




}
