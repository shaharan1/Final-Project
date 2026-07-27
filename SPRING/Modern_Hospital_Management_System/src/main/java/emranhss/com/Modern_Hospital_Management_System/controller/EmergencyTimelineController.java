package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyTimelineRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyTimelineResponse;
import emranhss.com.Modern_Hospital_Management_System.service.EmergencyTimelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergency/timeline")
@CrossOrigin("*")
@RequiredArgsConstructor
public class EmergencyTimelineController {

    private final EmergencyTimelineService emergencyTimelineService;

    @GetMapping("/patient/{emergencyPatientId}")
    public ResponseEntity<List<EmergencyTimelineResponse>> getByEmergencyPatientId(
            @PathVariable Long emergencyPatientId) {
        return ResponseEntity.ok(emergencyTimelineService.getByEmergencyPatientId(emergencyPatientId));
    }

    @GetMapping
    public ResponseEntity<List<EmergencyTimelineResponse>> getAll() {
        return ResponseEntity.ok(emergencyTimelineService.getAll());
    }

    @PostMapping("/patient/{emergencyPatientId}")
    public ResponseEntity<EmergencyTimelineResponse> addEvent(
            @PathVariable Long emergencyPatientId,
            @RequestBody EmergencyTimelineRequest request) {
        return new ResponseEntity<>(emergencyTimelineService.addEvent(emergencyPatientId, request), HttpStatus.CREATED);
    }
}
