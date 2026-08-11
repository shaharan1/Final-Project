package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.entity.PharmacySale;
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
        Double totalSales = saleRepository.sumSalesByDateRange(start, end);
        long totalTransactions = saleRepository.countSalesByDateRange(start, end);
        Map<String, Object> r = new HashMap<>();
        r.put("date", date.toString());
        r.put("totalSales", totalSales);
        r.put("totalTransactions", totalTransactions);
        r.put("totalCount", totalTransactions);
        r.put("totalRevenue", totalSales);
        return ResponseEntity.ok(r);
    }

    @GetMapping("/monthly-sales")
    public ResponseEntity<Map<String, Object>> monthlySales(@RequestParam int year, @RequestParam int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        LocalDateTime s = LocalDateTime.of(start, LocalTime.MIN);
        LocalDateTime e = LocalDateTime.of(end, LocalTime.MAX);
        List<PharmacySale> sales = saleRepository.findByDateRange(s, e).stream()
                .filter(sale -> "PAID".equals(sale.getPaymentStatus()))
                .collect(Collectors.toList());

        double totalRevenue = 0.0;
        Map<LocalDate, double[]> dayMap = new LinkedHashMap<>();
        for (PharmacySale sale : sales) {
            totalRevenue += sale.getNetPayable() != null ? sale.getNetPayable() : 0.0;
            LocalDate day = sale.getSaleDate().toLocalDate();
            double[] acc = dayMap.computeIfAbsent(day, k -> new double[2]);
            acc[0] += 1;
            acc[1] += sale.getNetPayable() != null ? sale.getNetPayable() : 0.0;
        }

        List<Map<String, Object>> breakdown = new ArrayList<>();
        for (Map.Entry<LocalDate, double[]> entry : dayMap.entrySet()) {
            Map<String, Object> row = new HashMap<>();
            row.put("date", entry.getKey().toString());
            row.put("sales", (long) entry.getValue()[0]);
            row.put("revenue", entry.getValue()[1]);
            breakdown.add(row);
        }

        Map<String, Object> r = new HashMap<>();
        r.put("year", year);
        r.put("month", month);
        r.put("totalSales", totalRevenue);
        r.put("totalTransactions", (long) sales.size());
        r.put("totalCount", (long) sales.size());
        r.put("totalRevenue", totalRevenue);
        r.put("breakdown", breakdown);
        return ResponseEntity.ok(r);
    }

    @GetMapping("/purchase-report")
    public ResponseEntity<Map<String, Object>> purchaseReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        LocalDateTime s = LocalDateTime.of(start, LocalTime.MIN);
        LocalDateTime e = LocalDateTime.of(end, LocalTime.MAX);
        long count = purchaseRepository.findByDateRange(s, e).size();
        double totalAmount = purchaseRepository.sumPurchasesByDateRange(s, e);
        Map<String, Object> r = new HashMap<>();
        r.put("startDate", start.toString());
        r.put("endDate", end.toString());
        r.put("totalPurchases", count);
        r.put("totalTransactions", count);
        r.put("totalAmount", totalAmount);
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
