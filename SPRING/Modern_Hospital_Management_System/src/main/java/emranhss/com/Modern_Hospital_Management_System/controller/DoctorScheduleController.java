package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.entity.Doctor;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "*") // Prevents CORS blockages during local frontend development
@RequiredArgsConstructor
public class DoctorScheduleController {

    private final DoctorRepository doctorRepository;

    /**
     * Endpoint 1: Fetches doctors belonging to a specific specialty dropdown option.
     */
    @GetMapping("/specializations/{specialization}/doctors")
    public ResponseEntity<List<Doctor>> getDoctorsBySpecialization(@PathVariable String specialization) {
        List<Doctor> doctors = doctorRepository.findBySpecialization(specialization);
        return ResponseEntity.ok(doctors);
    }

    /**
     * Endpoint 2: Parses a doctor's 'dutyHours' (e.g., "09:00-12:00") into 30-minute interval blocks.
     */
    @GetMapping("/doctors/{doctorId}/schedule")
    public ResponseEntity<List<LocalTime>> getDoctorSchedule(
            @PathVariable Long doctorId,
            @RequestParam("date") String dateStr) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));

        List<LocalTime> availableSlots = new ArrayList<>();
        String dutyHours = doctor.getDutyHours();

        if (dutyHours != null && dutyHours.contains("-")) {
            try {
                String[] parts = dutyHours.split("-");
                LocalTime startTime = LocalTime.parse(parts[0].trim());
                LocalTime endTime = LocalTime.parse(parts[1].trim());

                while (startTime.isBefore(endTime)) {
                    availableSlots.add(startTime);
                    startTime = startTime.plusMinutes(30); // 30-minute intervals
                }
            } catch (Exception e) {
                // Safe fallbacks if parsing fails
                availableSlots.add(LocalTime.of(9, 0));
                availableSlots.add(LocalTime.of(10, 0));
                availableSlots.add(LocalTime.of(11, 0));
            }
        }
        return ResponseEntity.ok(availableSlots);
    }
}