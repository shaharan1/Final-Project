package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.entity.*;
import emranhss.com.Modern_Hospital_Management_System.enums.PaymentStatus;
import emranhss.com.Modern_Hospital_Management_System.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/billing-reports")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BillingReportController {

    private final PaymentRepository paymentRepository;
    private final BillingInvoiceRepository billingInvoiceRepository;
    private final PharmacySaleRepository pharmacySaleRepository;
    private final PharmacySaleItemRepository pharmacySaleItemRepository;
    private final TestsRepository testsRepository;
    private final InsuranceClaimRepository insuranceClaimRepository;
    private final PurchaseRepository purchaseRepository;
    private final DoctorRepository doctorRepository;

    @GetMapping("/doctor-revenue")
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<List<Map<String, Object>>> doctorRevenue() {
        List<BillingInvoice> invoices = billingInvoiceRepository.findAll();
        Map<String, List<BillingInvoice>> byDoctor = invoices.stream()
                .filter(i -> resolveDoctor(i) != null)
                .collect(Collectors.groupingBy(i -> doctorName(resolveDoctor(i)),
                        LinkedHashMap::new, Collectors.toList()));

        List<Map<String, Object>> result = new ArrayList<>();
        byDoctor.forEach((doctorName, docs) -> {
            double revenue = docs.stream()
                    .mapToDouble(i -> i.getNetAmount() != null ? i.getNetAmount() : 0.0)
                    .sum();
            long patients = docs.stream()
                    .filter(i -> i.getPatient() != null)
                    .map(i -> i.getPatient().getId())
                    .distinct()
                    .count();
            Doctor doctor = resolveDoctor(docs.get(0));
            String department = doctor.getDoctorDepartment() != null
                    ? doctor.getDoctorDepartment().getDepartmentName() : "General";

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("doctorName", doctorName.trim());
            row.put("department", department);
            row.put("revenue", Math.round(revenue * 100.0) / 100.0);
            row.put("patientCount", patients);
            row.put("averageBill", patients > 0 ? Math.round((revenue / patients) * 100.0) / 100.0 : 0.0);
            result.add(row);
        });

        result.sort((a, b) -> Double.compare(((Number) b.get("revenue")).doubleValue(),
                ((Number) a.get("revenue")).doubleValue()));
        return ResponseEntity.ok(result);
    }

    private Doctor resolveDoctor(BillingInvoice invoice) {
        if (invoice.getReferringDoctor() != null) {
            return invoice.getReferringDoctor();
        }
        if (invoice.getAdmittedPatient() != null
                && invoice.getAdmittedPatient().getPrimaryDoctor() != null) {
            return invoice.getAdmittedPatient().getPrimaryDoctor();
        }
        return null;
    }

    private String doctorName(Doctor doctor) {
        if (doctor.getUser() != null && doctor.getUser().getName() != null
                && !doctor.getUser().getName().isBlank()) {
            return doctor.getUser().getName();
        }
        return doctor.getSpecialization() != null ? doctor.getSpecialization() : "Doctor " + doctor.getId();
    }

    @GetMapping("/pharmacy-revenue")
    public ResponseEntity<List<Map<String, Object>>> pharmacyRevenue() {
        List<PharmacySaleItem> items = pharmacySaleItemRepository.findAll();
        Map<String, List<PharmacySaleItem>> byMedicine = items.stream()
                .filter(i -> i.getPharmacySale() != null && "PAID".equals(i.getPharmacySale().getPaymentStatus()))
                .filter(i -> i.getMedicineStock() != null)
                .collect(Collectors.groupingBy(i -> i.getMedicineStock().getMedicineName(),
                        LinkedHashMap::new, Collectors.toList()));

        List<Map<String, Object>> result = new ArrayList<>();
        byMedicine.forEach((medicineName, list) -> {
            int quantity = list.stream().mapToInt(PharmacySaleItem::getQuantity).sum();
            double revenue = list.stream().mapToDouble(PharmacySaleItem::getSubtotal).sum();
            double cost = list.stream()
                    .mapToDouble(i -> i.getQuantity() * i.getMedicineStock().getPurchasePrice())
                    .sum();

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("medicineName", medicineName);
            row.put("quantitySold", quantity);
            row.put("revenue", Math.round(revenue * 100.0) / 100.0);
            row.put("cost", Math.round(cost * 100.0) / 100.0);
            row.put("profit", Math.round((revenue - cost) * 100.0) / 100.0);
            result.add(row);
        });

        result.sort((a, b) -> Double.compare(((Number) b.get("revenue")).doubleValue(),
                ((Number) a.get("revenue")).doubleValue()));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/lab-revenue")
    public ResponseEntity<List<Map<String, Object>>> labRevenue() {
        List<Tests> tests = testsRepository.findAll();
        Map<String, List<Tests>> byTest = tests.stream()
                .filter(t -> t.getTestMaster() != null)
                .collect(Collectors.groupingBy(t -> t.getTestMaster().getTestName(),
                        LinkedHashMap::new, Collectors.toList()));

        List<Map<String, Object>> result = new ArrayList<>();
        byTest.forEach((testName, list) -> {
            long count = list.size();
            double revenue = list.stream().mapToDouble(t -> t.getTestMaster().getStandardPrice()).sum();

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("testName", testName);
            row.put("count", count);
            row.put("revenue", Math.round(revenue * 100.0) / 100.0);
            row.put("averagePrice", count > 0 ? Math.round((revenue / count) * 100.0) / 100.0 : 0.0);
            result.add(row);
        });

        result.sort((a, b) -> Long.compare((Long) b.get("count"), (Long) a.get("count")));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/insurance-report")
    public ResponseEntity<List<Map<String, Object>>> insuranceReport() {
        List<InsuranceClaim> claims = insuranceClaimRepository.findAll();
        List<Map<String, Object>> result = claims.stream().map(c -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("invoiceNumber", c.getInvoiceNumber() != null ? c.getInvoiceNumber() : c.getClaimReference());
            row.put("patientName", c.getPatientName());
            row.put("provider", c.getInsuranceCompanyName() != null ? c.getInsuranceCompanyName() : "N/A");
            row.put("claimed", c.getClaimAmount() != null ? c.getClaimAmount() : 0.0);
            row.put("approved", c.getApprovedAmount() != null ? c.getApprovedAmount() : 0.0);
            row.put("status", c.getClaimStatus() != null ? c.getClaimStatus().toString() : "N/A");
            row.put("claimDate", c.getCreatedDate() != null ? c.getCreatedDate().toLocalDate().toString() : "");
            return row;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/pending-due")
    public ResponseEntity<List<Map<String, Object>>> pendingDue() {
        List<BillingInvoice> invoices = billingInvoiceRepository.findAll().stream()
                .filter(i -> i.getDueAmount() != null && i.getDueAmount() > 0
                        && !"CANCELLED".equals(i.getInvoiceStatus()))
                .collect(Collectors.toList());

        List<Map<String, Object>> result = invoices.stream().map(i -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("patientName", i.getPatient() != null ? i.getPatient().getName() : "N/A");
            row.put("invoiceNumber", i.getInvoiceNumber());
            row.put("total", i.getNetAmount() != null ? i.getNetAmount() : 0.0);
            row.put("paid", i.getTotalPaid() != null ? i.getTotalPaid() : 0.0);
            row.put("due", i.getDueAmount());
            row.put("dueDate", i.getCreatedDate() != null ? i.getCreatedDate().toLocalDate().toString() : "");
            long daysOverdue = i.getCreatedDate() != null
                    ? ChronoUnit.DAYS.between(i.getCreatedDate().toLocalDate(), LocalDate.now())
                    : 0L;
            row.put("daysOverdue", Math.max(daysOverdue, 0L));
            return row;
        }).collect(Collectors.toList());

        result.sort((a, b) -> Long.compare((Long) b.get("daysOverdue"), (Long) a.get("daysOverdue")));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/profit-loss")
    public ResponseEntity<List<Map<String, Object>>> profitLoss(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        YearMonth current = (year != null && month != null)
                ? YearMonth.of(year, month)
                : YearMonth.now();
        YearMonth start = current.minusMonths(5);

        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            YearMonth ym = current.minusMonths(i);
            LocalDateTime s = ym.atDay(1).atStartOfDay();
            LocalDateTime e = ym.atEndOfMonth().atTime(LocalTime.MAX);

            double revenue = paymentRepository.findByPaymentDateBetween(s, e).stream()
                    .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED)
                    .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                    .sum();

            double expenses = purchaseRepository.sumPurchasesByDateRange(s, e) != null
                    ? purchaseRepository.sumPurchasesByDateRange(s, e)
                    : 0.0;

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("month", ym.atDay(1).toString());
            row.put("revenue", Math.round(revenue * 100.0) / 100.0);
            row.put("expenses", Math.round(expenses * 100.0) / 100.0);
            row.put("profit", Math.round((revenue - expenses) * 100.0) / 100.0);
            result.add(row);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/monthly-collection")
    public ResponseEntity<Map<String, Object>> monthlyCollection(
            @RequestParam int year, @RequestParam int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        LocalDateTime s = startDate.atStartOfDay();
        LocalDateTime e = endDate.atTime(LocalTime.MAX);

        List<Payment> payments = paymentRepository.findByPaymentDateBetween(s, e).stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED)
                .collect(Collectors.toList());

        double totalRevenue = payments.stream()
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                .sum();

        Map<LocalDate, List<Payment>> byDay = payments.stream()
                .collect(Collectors.groupingBy(p -> p.getPaymentDate().toLocalDate(),
                        LinkedHashMap::new, Collectors.toList()));

        List<Map<String, Object>> breakdown = new ArrayList<>();
        for (int day = 1; day <= endDate.getDayOfMonth(); day++) {
            LocalDate date = LocalDate.of(year, month, day);
            List<Payment> dayPayments = byDay.getOrDefault(date, List.of());
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("date", date.toString());
            row.put("revenue", dayPayments.stream()
                    .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                    .sum());
            row.put("bills", dayPayments.size());
            breakdown.add(row);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("year", year);
        result.put("month", month);
        result.put("totalRevenue", Math.round(totalRevenue * 100.0) / 100.0);
        result.put("totalBills", payments.size());
        result.put("dailyBreakdown", breakdown);
        return ResponseEntity.ok(result);
    }
}
