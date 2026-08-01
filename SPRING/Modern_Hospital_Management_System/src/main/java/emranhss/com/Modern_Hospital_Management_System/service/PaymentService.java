package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.entity.Payment;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PaymentService {

    List<Payment> getAll();

    Payment getById(Long id);

    List<Payment> getByInvoiceNumber(String invoiceNumber);

    List<Payment> getByPatientId(Long patientId);

    Payment processPayment(Payment payment);

    Payment updatePayment(Payment payment);

    void deletePayment(Long id);

    Map<String, Object> getDashboardStats();

    Map<String, Object> getDailyRevenue(LocalDate date);

    Map<String, Object> getMonthlyRevenue(int year, int month);

    List<Map<String, Object>> getPaymentMethodBreakdown();

    List<Payment> getUnpaidByPatientId(Long patientId);

    List<Payment> getUnpaidByPatientName(String patientName);

    List<Payment> searchUnpaid(String search);
}
