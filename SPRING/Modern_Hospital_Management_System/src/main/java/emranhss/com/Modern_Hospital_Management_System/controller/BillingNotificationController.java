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
        return ResponseEntity.ok(billingNotificationService.getAllNotifications());
    }

    @GetMapping("/unread")
    public ResponseEntity<List<BillingNotification>> getUnread() {
        return ResponseEntity.ok(billingNotificationService.getUnreadNotifications());
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount() {
        return ResponseEntity.ok(Map.of("count", billingNotificationService.getUnreadCount()));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<BillingNotification> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(billingNotificationService.markAsRead(id));
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
