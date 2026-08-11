package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.Payment;
import emranhss.com.Modern_Hospital_Management_System.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByInvoiceNumber(String invoiceNumber);

    List<Payment> findByPatientId(Long patientId);

    List<Payment> findByPaymentStatus(PaymentStatus status);

    List<Payment> findByPaymentDateBetween(LocalDateTime start, LocalDateTime end);

    List<Payment> findByPaymentStatusAndPatientId(PaymentStatus status, Long patientId);

    List<Payment> findByPaymentStatusAndPatientNameContainingIgnoreCase(PaymentStatus status, String patientName);

    @Query("SELECT p FROM Payment p WHERE p.paymentStatus = :status AND " +
           "(LOWER(p.patientName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "CAST(p.patientId AS string) LIKE CONCAT('%', :search, '%') OR " +
           "LOWER(p.invoiceNumber) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Payment> searchUnpaid(@Param("search") String search, @Param("status") PaymentStatus status);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentDate BETWEEN :start AND :end AND p.paymentStatus = :status")
    Double sumAmountByDateRangeAndStatus(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("status") PaymentStatus status);

    @Query("SELECT p.paymentStatus as status, COUNT(p) as count FROM Payment p GROUP BY p.paymentStatus")
    List<Object[]> countByPaymentStatus();

    @Query("SELECT FUNCTION('DATE', p.paymentDate) as date, SUM(p.amount) as total FROM Payment p WHERE p.paymentStatus = 'COMPLETED' GROUP BY FUNCTION('DATE', p.paymentDate)")
    List<Object[]> totalRevenueByDate();

    @Query("SELECT p.paymentMethod as method, SUM(p.amount) as total, COUNT(p) as count FROM Payment p WHERE p.paymentStatus = 'COMPLETED' GROUP BY p.paymentMethod")
    List<Object[]> paymentMethodBreakdown();
}
