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
    private final BillingInvoiceRepository billingInvoiceRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new LinkedHashMap<>();

        List<Payment> allPayments = paymentRepository.findAll();
        List<Refund> allRefunds = refundRepository.findAll();
        List<InsuranceClaim> allClaims = insuranceClaimRepository.findAll();
        List<BillingInvoice> allInvoices = billingInvoiceRepository.findAll();

        LocalDate today = LocalDate.now();

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
                        && p.getPaymentDate().toLocalDate().equals(today))
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                .sum();

        double yesterdayRevenue = allPayments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED
                        && p.getPaymentDate() != null
                        && p.getPaymentDate().toLocalDate().equals(today.minusDays(1)))
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                .sum();

        YearMonth currentMonth = YearMonth.from(today);

        double monthRevenue = allPayments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED
                        && p.getPaymentDate() != null
                        && YearMonth.from(p.getPaymentDate().toLocalDate()).equals(currentMonth))
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                .sum();

        double prevMonthRevenue = allPayments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED
                        && p.getPaymentDate() != null
                        && YearMonth.from(p.getPaymentDate().toLocalDate()).equals(currentMonth.minusMonths(1)))
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                .sum();

        long pendingPayments = allPayments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.PENDING)
                .count();

        long paidBills = allPayments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED)
                .count();

        long unpaidBills = allInvoices.stream()
                .filter(i -> i.getDueAmount() != null && i.getDueAmount() > 0
                        && !"CANCELLED".equals(i.getInvoiceStatus()))
                .count();

        long pendingClaims = allClaims.stream()
                .filter(c -> c.getClaimStatus() == ClaimStatus.SUBMITTED || c.getClaimStatus() == ClaimStatus.UNDER_REVIEW)
                .count();

        long pendingRefunds = allRefunds.stream()
                .filter(r -> r.getRefundStatus() == RefundStatus.PENDING)
                .count();

        long totalPatientsBilled = allPayments.stream()
                .map(Payment::getPatientId)
                .distinct()
                .count();

        double todayRevenueChange = yesterdayRevenue > 0
                ? Math.round(((todayRevenue - yesterdayRevenue) / yesterdayRevenue) * 10000.0) / 100.0
                : (todayRevenue > 0 ? 100.0 : 0.0);

        double monthlyRevenueChange = prevMonthRevenue > 0
                ? Math.round(((monthRevenue - prevMonthRevenue) / prevMonthRevenue) * 10000.0) / 100.0
                : (monthRevenue > 0 ? 100.0 : 0.0);

        summary.put("totalRevenue", totalRevenue);
        summary.put("totalRefunds", totalRefunds);
        summary.put("netRevenue", netRevenue);
        summary.put("todayRevenue", todayRevenue);
        summary.put("monthRevenue", monthRevenue);
        summary.put("monthlyRevenue", monthRevenue);
        summary.put("totalPayments", allPayments.size());
        summary.put("totalRefundCount", allRefunds.size());
        summary.put("pendingClaims", pendingClaims);
        summary.put("pendingRefunds", pendingRefunds);
        summary.put("totalOutstandingInvoices", invoiceRepository.count());
        summary.put("totalAdmittedPatientInvoices", admitPatientInvoiceRepository.count());
        summary.put("pendingPayments", pendingPayments);
        summary.put("paidBills", paidBills);
        summary.put("unpaidBills", unpaidBills);
        summary.put("insuranceClaims", pendingClaims);
        summary.put("refundAmount", totalRefunds);
        summary.put("totalPatientsBilled", totalPatientsBilled);
        summary.put("todayRevenueChange", todayRevenueChange);
        summary.put("monthlyRevenueChange", monthlyRevenueChange);

        return summary;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getDailyRevenueChart() {
        List<Map<String, Object>> chartData = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 6; i >= 0; i--) {
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

        for (int i = 5; i >= 0; i--) {
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
            point.put("date", month.atDay(1).toString());
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
