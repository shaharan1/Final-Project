package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.entity.Payment;
import emranhss.com.Modern_Hospital_Management_System.enums.PaymentStatus;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.PaymentRepository;
import emranhss.com.Modern_Hospital_Management_System.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImp implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Payment getById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getByInvoiceNumber(String invoiceNumber) {
        return paymentRepository.findByInvoiceNumber(invoiceNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getByPatientId(Long patientId) {
        return paymentRepository.findByPatientId(patientId);
    }

    @Override
    @Transactional
    public Payment processPayment(Payment payment) {
        payment.setPaymentReference(generatePaymentReference());
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDateTime.now());
        }
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public Payment updatePayment(Payment payment) {
        Payment existing = paymentRepository.findById(payment.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + payment.getId()));
        existing.setPaymentMethod(payment.getPaymentMethod());
        existing.setPaymentStatus(payment.getPaymentStatus());
        existing.setAmount(payment.getAmount());
        existing.setNotes(payment.getNotes());
        existing.setTransactionId(payment.getTransactionId());
        existing.setCardLast4(payment.getCardLast4());
        existing.setBankName(payment.getBankName());
        existing.setMobileProvider(payment.getMobileProvider());
        existing.setDiscount(payment.getDiscount());
        existing.setVAT(payment.getVAT());
        existing.setNetAmount(payment.getNetAmount());
        existing.setRefundAmount(payment.getRefundAmount());
        return paymentRepository.save(existing);
    }

    @Override
    @Transactional
    public void deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payment not found with ID: " + id);
        }
        paymentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        List<Payment> allPayments = paymentRepository.findAll();

        double totalRevenue = allPayments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED)
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                .sum();

        long totalTransactions = allPayments.size();
        long completedPayments = allPayments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED)
                .count();
        long pendingPayments = allPayments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.PENDING)
                .count();
        long failedPayments = allPayments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.FAILED)
                .count();

        double todayRevenue = allPayments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED
                        && p.getPaymentDate() != null
                        && p.getPaymentDate().toLocalDate().equals(LocalDate.now()))
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                .sum();

        stats.put("totalRevenue", totalRevenue);
        stats.put("totalTransactions", totalTransactions);
        stats.put("completedPayments", completedPayments);
        stats.put("pendingPayments", pendingPayments);
        stats.put("failedPayments", failedPayments);
        stats.put("todayRevenue", todayRevenue);
        stats.put("averagePaymentAmount", totalTransactions > 0 ? totalRevenue / completedPayments : 0.0);

        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDailyRevenue(LocalDate date) {
        Map<String, Object> result = new LinkedHashMap<>();
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Payment> dayPayments = paymentRepository.findByPaymentDateBetween(start, end);
        List<Payment> completed = dayPayments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED)
                .collect(Collectors.toList());

        double totalRevenue = completed.stream()
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                .sum();

        result.put("date", date.toString());
        result.put("totalRevenue", totalRevenue);
        result.put("totalTransactions", dayPayments.size());
        result.put("completedTransactions", completed.size());
        result.put("payments", completed);

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getMonthlyRevenue(int year, int month) {
        Map<String, Object> result = new LinkedHashMap<>();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        List<Payment> monthPayments = paymentRepository.findByPaymentDateBetween(start, end);
        List<Payment> completed = monthPayments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED)
                .collect(Collectors.toList());

        double totalRevenue = completed.stream()
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                .sum();

        Map<LocalDate, List<Payment>> byDay = completed.stream()
                .collect(Collectors.groupingBy(p -> p.getPaymentDate().toLocalDate(), LinkedHashMap::new, Collectors.toList()));

        List<Map<String, Object>> dailyBreakdown = new ArrayList<>();
        int daysInMonth = endDate.getDayOfMonth();
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = LocalDate.of(year, month, day);
            List<Payment> dayPayments = byDay.getOrDefault(date, List.of());
            double dayRevenue = dayPayments.stream()
                    .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                    .sum();
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("date", date.toString());
            row.put("revenue", dayRevenue);
            row.put("bills", dayPayments.size());
            dailyBreakdown.add(row);
        }

        result.put("year", year);
        result.put("month", month);
        result.put("totalRevenue", totalRevenue);
        result.put("totalTransactions", monthPayments.size());
        result.put("completedTransactions", completed.size());
        result.put("totalBills", completed.size());
        result.put("dailyBreakdown", dailyBreakdown);

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPaymentMethodBreakdown() {
        List<Object[]> breakdown = paymentRepository.paymentMethodBreakdown();
        return breakdown.stream().map(row -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("method", row[0] != null ? row[0].toString() : "UNKNOWN");
            map.put("totalAmount", row[1] != null ? row[1] : 0.0);
            map.put("count", row[2] != null ? row[2] : 0L);
            return map;
        }).collect(Collectors.toList());
    }

    private String generatePaymentReference() {
        long year = Year.now().getValue();
        long count = paymentRepository.count() + 1;
        return String.format("PAY-%d-%04d", year, count);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getUnpaidByPatientId(Long patientId) {
        return paymentRepository.findByPaymentStatusAndPatientId(PaymentStatus.PENDING, patientId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getUnpaidByPatientName(String patientName) {
        return paymentRepository.findByPaymentStatusAndPatientNameContainingIgnoreCase(PaymentStatus.PENDING, patientName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> searchUnpaid(String search) {
        if (search == null || search.isBlank()) {
            return paymentRepository.findByPaymentStatus(PaymentStatus.PENDING);
        }
        return paymentRepository.searchUnpaid(search, PaymentStatus.PENDING);
    }
}
