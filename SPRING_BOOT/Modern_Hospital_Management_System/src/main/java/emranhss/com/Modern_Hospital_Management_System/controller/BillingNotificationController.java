package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.entity.BillingNotification;
import emranhss.com.Modern_Hospital_Management_System.service.BillingNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/billing-notifications")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BillingNotificationController {

    private final BillingNotificationService billingNotificationService;

    @GetMapping
    public ResponseEntity<List<BillingNotification>> getAll() {
        return ResponseEntity.ok(billingNotificationService.getAll());
    }

    @GetMapping("/unread")
    public ResponseEntity<List<BillingNotification>> getUnread() {
        return ResponseEntity.ok(billingNotificationService.getUnread());
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount() {
        return ResponseEntity.ok(Map.of("count", billingNotificationService.getUnreadCount()));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        billingNotificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead() {
        billingNotificationService.markAllAsRead();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        billingNotificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
