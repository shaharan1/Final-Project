package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.entity.*;
import emranhss.com.Modern_Hospital_Management_System.enums.ClaimStatus;
import emranhss.com.Modern_Hospital_Management_System.enums.PaymentStatus;
import emranhss.com.Modern_Hospital_Management_System.enums.RefundStatus;
import emranhss.com.Modern_Hospital_Management_System.repository.*;
import emranhss.com.Modern_Hospital_Management_System.service.BillingDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillingDashboardServiceImp implements BillingDashboardService {

    private final InvoiceRepository invoiceRepository;
    private final AdmitPatientInvoiceRepository admitPatientInvoiceRepository;
    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;
    private final InsuranceClaimRepository insuranceClaimRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new LinkedHashMap<>();

        List<Payment> allPayments = paymentRepository.findAll();
        List<Refund> allRefunds = refundRepository.findAll();
        List<InsuranceClaim> allClaims = insuranceClaimRepository.findAll();

        double totalRevenue = allPayments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED)
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                .sum();

        double totalRefunds = allRefunds.stream()
                .filter(r -> r.getRefundStatus() == RefundStatus.PROCESSED)
                .mapToDouble(r -> r.getRefundAmount() != null ? r.getRefundAmount() : 0.0)
                .sum();

        double netRevenue = totalRevenue - totalRefunds;

        double todayRevenue = allPayments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED
                        && p.getPaymentDate() != null
                        && p.getPaymentDate().toLocalDate().equals(LocalDate.now()))
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                .sum();

        double monthRevenue = allPayments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED
                        && p.getPaymentDate() != null
                        && p.getPaymentDate().toLocalDate().getMonth() == LocalDate.now().getMonth()
                        && p.getPaymentDate().toLocalDate().getYear() == LocalDate.now().getYear())
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                .sum();

        long pendingClaims = allClaims.stream()
                .filter(c -> c.getClaimStatus() == ClaimStatus.SUBMITTED || c.getClaimStatus() == ClaimStatus.UNDER_REVIEW)
                .count();

        long pendingRefunds = allRefunds.stream()
                .filter(r -> r.getRefundStatus() == RefundStatus.PENDING)
                .count();

        summary.put("totalRevenue", totalRevenue);
        summary.put("totalRefunds", totalRefunds);
        summary.put("netRevenue", netRevenue);
        summary.put("todayRevenue", todayRevenue);
        summary.put("monthRevenue", monthRevenue);
        summary.put("totalPayments", allPayments.size());
        summary.put("totalRefundCount", allRefunds.size());
        summary.put("pendingClaims", pendingClaims);
        summary.put("pendingRefunds", pendingRefunds);
        summary.put("totalOutstandingInvoices", invoiceRepository.count());
        summary.put("totalAdmittedPatientInvoices", admitPatientInvoiceRepository.count());

        return summary;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getDailyRevenueChart() {
        List<Map<String, Object>> chartData = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 29; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(LocalTime.MAX);

            List<Payment> dayPayments = paymentRepository.findByPaymentDateBetween(start, end);
            double revenue = dayPayments.stream()
                    .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED)
                    .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                    .sum();

            Map<String, Object> point = new LinkedHashMap<>();
            point.put("date", date.toString());
            point.put("revenue", revenue);
            point.put("transactions", dayPayments.size());
            chartData.add(point);
        }
        return chartData;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMonthlyRevenueChart() {
        List<Map<String, Object>> chartData = new ArrayList<>();
        YearMonth current = YearMonth.now();

        for (int i = 11; i >= 0; i--) {
            YearMonth month = current.minusMonths(i);
            LocalDate startDate = month.atDay(1);
            LocalDate endDate = month.atEndOfMonth();
            LocalDateTime start = startDate.atStartOfDay();
            LocalDateTime end = endDate.atTime(LocalTime.MAX);

            List<Payment> monthPayments = paymentRepository.findByPaymentDateBetween(start, end);
            double revenue = monthPayments.stream()
                    .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED)
                    .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                    .sum();

            Map<String, Object> point = new LinkedHashMap<>();
            point.put("month", month.toString());
            point.put("revenue", revenue);
            point.put("transactions", monthPayments.size());
            chartData.add(point);
        }
        return chartData;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getDepartmentRevenue() {
        List<Map<String, Object>> departmentData = new ArrayList<>();

        List<AdmitPatientInvoice> invoices = admitPatientInvoiceRepository.findAll();
        Map<String, Double> departmentRevenue = new LinkedHashMap<>();

        for (AdmitPatientInvoice invoice : invoices) {
            String department = "General";
            if (invoice.getAdmittedPatient() != null
                    && invoice.getAdmittedPatient().getPrimaryDoctor() != null
                    && invoice.getAdmittedPatient().getPrimaryDoctor().getDoctorDepartment() != null) {
                department = invoice.getAdmittedPatient().getPrimaryDoctor().getDoctorDepartment().getDepartmentName();
            }
            departmentRevenue.merge(department, invoice.getPaidAmount(), Double::sum);
        }

        departmentRevenue.forEach((dept, revenue) -> {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("department", dept);
            entry.put("revenue", revenue);
            departmentData.add(entry);
        });

        return departmentData;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPaymentMethodDistribution() {
        List<Object[]> breakdown = paymentRepository.paymentMethodBreakdown();
        return breakdown.stream().map(row -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("method", row[0] != null ? row[0].toString() : "UNKNOWN");
            map.put("totalAmount", row[1] != null ? row[1] : 0.0);
            map.put("count", row[2] != null ? row[2] : 0L);
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getRecentActivity() {
        List<Map<String, Object>> activities = new ArrayList<>();

        List<Payment> recentPayments = paymentRepository.findAll().stream()
                .sorted(Comparator.comparing(Payment::getCreatedDate).reversed())
                .limit(10)
                .collect(Collectors.toList());

        for (Payment payment : recentPayments) {
            Map<String, Object> activity = new LinkedHashMap<>();
            activity.put("type", "PAYMENT");
            activity.put("id", payment.getId());
            activity.put("reference", payment.getPaymentReference());
            activity.put("patientName", payment.getPatientName());
            activity.put("amount", payment.getAmount());
            activity.put("status", payment.getPaymentStatus().toString());
            activity.put("date", payment.getCreatedDate() != null ? payment.getCreatedDate().toString() : null);
            activities.add(activity);
        }

        List<Refund> recentRefunds = refundRepository.findAll().stream()
                .sorted(Comparator.comparing(Refund::getCreatedDate).reversed())
                .limit(10)
                .collect(Collectors.toList());

        for (Refund refund : recentRefunds) {
            Map<String, Object> activity = new LinkedHashMap<>();
            activity.put("type", "REFUND");
            activity.put("id", refund.getId());
            activity.put("reference", refund.getRefundReference());
            activity.put("patientName", refund.getPatientName());
            activity.put("amount", refund.getRefundAmount());
            activity.put("status", refund.getRefundStatus().toString());
            activity.put("date", refund.getCreatedDate() != null ? refund.getCreatedDate().toString() : null);
            activities.add(activity);
        }

        activities.sort((a, b) -> {
            String dateA = (String) a.get("date");
            String dateB = (String) b.get("date");
            if (dateA == null) return 1;
            if (dateB == null) return -1;
            return dateB.compareTo(dateA);
        });

        return activities.stream().limit(20).collect(Collectors.toList());
    }
}
