package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.service.BillingDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/billing-dashboard")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BillingDashboardController {

    private final BillingDashboardService billingDashboardService;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getDashboardSummary() {
        return ResponseEntity.ok(billingDashboardService.getDashboardSummary());
    }

    @GetMapping("/daily-revenue-chart")
    public ResponseEntity<List<Map<String, Object>>> getDailyRevenueChart() {
        return ResponseEntity.ok(billingDashboardService.getDailyRevenueChart());
    }

    @GetMapping("/monthly-revenue-chart")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyRevenueChart() {
        return ResponseEntity.ok(billingDashboardService.getMonthlyRevenueChart());
    }

    @GetMapping("/department-revenue")
    public ResponseEntity<List<Map<String, Object>>> getDepartmentRevenue() {
        return ResponseEntity.ok(billingDashboardService.getDepartmentRevenue());
    }

    @GetMapping("/payment-methods")
    public ResponseEntity<List<Map<String, Object>>> getPaymentMethodDistribution() {
        return ResponseEntity.ok(billingDashboardService.getPaymentMethodDistribution());
    }

    @GetMapping("/recent-activity")
    public ResponseEntity<List<Map<String, Object>>> getRecentActivity() {
        return ResponseEntity.ok(billingDashboardService.getRecentActivity());
    }
}
