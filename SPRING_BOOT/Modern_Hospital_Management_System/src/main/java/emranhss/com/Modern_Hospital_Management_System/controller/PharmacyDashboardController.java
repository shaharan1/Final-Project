package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.response.PharmacyDashboardResponse;
import emranhss.com.Modern_Hospital_Management_System.repository.*;
import emranhss.com.Modern_Hospital_Management_System.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pharmacy/dashboard")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PharmacyDashboardController {

    private final PharmacySaleRepository saleRepository;
    private final PurchaseRepository purchaseRepository;
    private final MedicineStockRepository stockRepository;
    private final StockService stockService;

    @GetMapping
    public ResponseEntity<PharmacyDashboardResponse> getDashboard() {
        PharmacyDashboardResponse d = new PharmacyDashboardResponse();
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime monthStart = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
        d.setTodaySales(saleRepository.sumSalesByDateRange(todayStart, todayEnd));
        d.setTodaySalesCount(saleRepository.countSalesByDateRange(todayStart, todayEnd));
        d.setTodayPurchases(purchaseRepository.sumPurchasesByDateRange(todayStart, todayEnd));
        d.setTodayPurchasesCount((long) purchaseRepository.findByDateRange(todayStart, todayEnd).size());
        d.setTotalMedicines(stockRepository.countActiveMedicines());
        d.setTotalAvailableStock(stockRepository.totalAvailableStock());
        d.setLowStockCount((long) stockService.getLowStock().size());
        d.setExpiredCount((long) stockService.getExpired().size());
        d.setExpiringSoonCount((long) stockService.getExpiringSoon(30).size());
        d.setMonthlyRevenue(saleRepository.sumSalesByDateRange(monthStart, todayEnd));
        d.setMonthlyProfit(saleRepository.sumProfitByDateRange(monthStart, todayEnd));
        List<emranhss.com.Modern_Hospital_Management_System.entity.PharmacySale> recentSales = saleRepository.findAllByOrderBySaleDateDesc();
        d.setRecentSales(recentSales.stream().limit(10).map(s -> {
            emranhss.com.Modern_Hospital_Management_System.dto.response.PharmacySaleResponse r = new emranhss.com.Modern_Hospital_Management_System.dto.response.PharmacySaleResponse();
            r.setId(s.getId());
            r.setSaleInvoiceNo(s.getSaleInvoiceNo());
            r.setPatientName(s.getPatientName());
            r.setNetPayable(s.getNetPayable());
            r.setPaymentStatus(s.getPaymentStatus());
            r.setSaleDate(s.getSaleDate());
            return r;
        }).collect(Collectors.toList()));
        return ResponseEntity.ok(d);
    }

    @GetMapping("/expiry-alerts")
    public ResponseEntity<List<Map<String, Object>>> getExpiryAlerts() {
        List<Map<String, Object>> alerts = new ArrayList<>();
        stockService.getExpiringSoon(90).forEach(s -> {
            Map<String, Object> a = new HashMap<>();
            a.put("id", s.getId());
            a.put("medicineName", s.getMedicineName());
            a.put("batchNumber", s.getBatchNumber());
            a.put("expiryDate", s.getExpiryDate());
            a.put("stockQuantity", s.getAvailableQuantity());
            a.put("supplierName", s.getSupplierName());
            alerts.add(a);
        });
        return ResponseEntity.ok(alerts);
    }
}
