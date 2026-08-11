package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.response.DoctorNotificationResponse;
import emranhss.com.Modern_Hospital_Management_System.service.lab.DoctorNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lab/notifications")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class LabNotificationController {

    private final DoctorNotificationService doctorNotificationService;

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorNotificationResponse>> getByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorNotificationService.getByDoctor(doctorId));
    }

    @GetMapping("/doctor/{doctorId}/unread")
    public ResponseEntity<List<DoctorNotificationResponse>> getUnreadByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorNotificationService.getUnreadByDoctor(doctorId));
    }

    @GetMapping("/doctor/{doctorId}/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@PathVariable Long doctorId) {
        return ResponseEntity.ok(Map.of("count", doctorNotificationService.getUnreadCount(doctorId)));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markRead(@PathVariable Long id) {
        doctorNotificationService.markRead(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/read-all/doctor/{doctorId}")
    public ResponseEntity<Void> markAllRead(@PathVariable Long doctorId) {
        doctorNotificationService.markAllRead(doctorId);
        return ResponseEntity.noContent().build();
    }
}
