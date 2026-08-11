package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/dashboard-summary")
    public ResponseEntity<Map<String, Object>> getDashboardSummary() {
        return ResponseEntity.ok(reportService.getDashboardSummary());
    }

    @GetMapping("/patient/analytics")
    public ResponseEntity<Map<String, Object>> getPatientAnalytics() {
        return ResponseEntity.ok(reportService.getPatientAnalytics());
    }

    @GetMapping("/appointment/analytics")
    public ResponseEntity<Map<String, Object>> getAppointmentAnalytics() {
        return ResponseEntity.ok(reportService.getAppointmentAnalytics());
    }

    @GetMapping("/doctor/analytics")
    public ResponseEntity<Map<String, Object>> getDoctorAnalytics() {
        return ResponseEntity.ok(reportService.getDoctorAnalytics());
    }

    @GetMapping("/laboratory/analytics")
    public ResponseEntity<Map<String, Object>> getLabAnalytics() {
        return ResponseEntity.ok(reportService.getLabAnalytics());
    }

    @GetMapping("/pharmacy/analytics")
    public ResponseEntity<Map<String, Object>> getPharmacyAnalytics() {
        return ResponseEntity.ok(reportService.getPharmacyAnalytics());
    }

    @GetMapping("/revenue/analytics")
    public ResponseEntity<Map<String, Object>> getRevenueAnalytics() {
        return ResponseEntity.ok(reportService.getRevenueAnalytics());
    }

    @GetMapping("/bed/occupancy")
    public ResponseEntity<Map<String, Object>> getBedOccupancy() {
        return ResponseEntity.ok(reportService.getBedOccupancy());
    }

    @GetMapping("/emergency/analytics")
    public ResponseEntity<Map<String, Object>> getEmergencyAnalytics() {
        return ResponseEntity.ok(reportService.getEmergencyAnalytics());
    }

    @GetMapping("/financial/analytics")
    public ResponseEntity<Map<String, Object>> getFinancialAnalytics() {
        return ResponseEntity.ok(reportService.getFinancialAnalytics());
    }

    @GetMapping("/daily-revenue")
    public ResponseEntity<Map<String, Object>> getDailyRevenue(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(reportService.getDailyRevenue(date));
    }

    @GetMapping("/monthly-revenue")
    public ResponseEntity<Map<String, Object>> getMonthlyRevenue(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(reportService.getMonthlyRevenue(year, month));
    }

    @GetMapping("/department-revenue")
    public ResponseEntity<List<Map<String, Object>>> getDepartmentRevenue() {
        return ResponseEntity.ok(reportService.getDepartmentRevenue());
    }

    @GetMapping("/top-doctors")
    public ResponseEntity<List<Map<String, Object>>> getTopDoctors() {
        return ResponseEntity.ok(reportService.getTopDoctors());
    }

    @GetMapping("/top-medicines")
    public ResponseEntity<List<Map<String, Object>>> getTopMedicines() {
        return ResponseEntity.ok(reportService.getTopMedicines());
    }

    @GetMapping("/recent-activity")
    public ResponseEntity<List<Map<String, Object>>> getRecentActivity() {
        return ResponseEntity.ok(reportService.getRecentActivity());
    }
}
