package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.repository.*;
import emranhss.com.Modern_Hospital_Management_System.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pharmacy/reports")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PharmacyReportController {

    private final PharmacySaleRepository saleRepository;
    private final PurchaseRepository purchaseRepository;
    private final MedicineStockRepository stockRepository;
    private final SupplierRepository supplierRepository;
    private final StockService stockService;

    @GetMapping("/daily-sales")
    public ResponseEntity<Map<String, Object>> dailySales(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDateTime start = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX);
        Map<String, Object> r = new HashMap<>();
        r.put("date", date.toString());
        r.put("totalSales", saleRepository.sumSalesByDateRange(start, end));
        r.put("totalTransactions", saleRepository.countSalesByDateRange(start, end));
        return ResponseEntity.ok(r);
    }

    @GetMapping("/monthly-sales")
    public ResponseEntity<Map<String, Object>> monthlySales(@RequestParam int year, @RequestParam int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        LocalDateTime s = LocalDateTime.of(start, LocalTime.MIN);
        LocalDateTime e = LocalDateTime.of(end, LocalTime.MAX);
        Map<String, Object> r = new HashMap<>();
        r.put("year", year);
        r.put("month", month);
        r.put("totalSales", saleRepository.sumSalesByDateRange(s, e));
        r.put("totalTransactions", saleRepository.countSalesByDateRange(s, e));
        return ResponseEntity.ok(r);
    }

    @GetMapping("/purchase-report")
    public ResponseEntity<Map<String, Object>> purchaseReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        LocalDateTime s = LocalDateTime.of(start, LocalTime.MIN);
        LocalDateTime e = LocalDateTime.of(end, LocalTime.MAX);
        Map<String, Object> r = new HashMap<>();
        r.put("startDate", start.toString());
        r.put("endDate", end.toString());
        r.put("totalPurchases", purchaseRepository.sumPurchasesByDateRange(s, e));
        r.put("totalTransactions", (long) purchaseRepository.findByDateRange(s, e).size());
        return ResponseEntity.ok(r);
    }

    @GetMapping("/stock-report")
    public ResponseEntity<Map<String, Object>> stockReport() {
        Map<String, Object> r = new HashMap<>();
        r.put("totalMedicines", stockRepository.countActiveMedicines());
        r.put("totalAvailableStock", stockRepository.totalAvailableStock());
        r.put("lowStock", stockService.getLowStock().size());
        r.put("expired", stockService.getExpired().size());
        r.put("expiringSoon", stockService.getExpiringSoon(30).size());
        return ResponseEntity.ok(r);
    }

    @GetMapping("/supplier-report")
    public ResponseEntity<List<Map<String, Object>>> supplierReport() {
        List<Map<String, Object>> report = new ArrayList<>();
        supplierRepository.findAll().forEach(s -> {
            Map<String, Object> r = new HashMap<>();
            r.put("id", s.getId());
            r.put("name", s.getName());
            r.put("totalDue", s.getTotalDue());
            r.put("active", s.getActive());
            report.add(r);
        });
        return ResponseEntity.ok(report);
    }
}
