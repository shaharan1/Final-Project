package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.BillingPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BillingPaymentRepository extends JpaRepository<BillingPayment, Long> {

    List<BillingPayment> findByInvoiceIdOrderByPaymentDateDesc(Long invoiceId);

    List<BillingPayment> findByPaymentStatusOrderByPaymentDateDesc(String paymentStatus);

    @Query("SELECT bp FROM BillingPayment bp WHERE bp.invoice.patient.id = :patientId ORDER BY bp.paymentDate DESC")
    List<BillingPayment> findByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT COALESCE(SUM(bp.amount), 0) FROM BillingPayment bp WHERE bp.paymentStatus = 'COMPLETED' AND bp.paymentDate BETWEEN :start AND :end")
    Double sumCompletedPaymentsByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT bp.paymentMethod, COUNT(bp), SUM(bp.amount) FROM BillingPayment bp WHERE bp.paymentStatus = 'COMPLETED' AND bp.paymentDate BETWEEN :start AND :end GROUP BY bp.paymentMethod")
    List<Object[]> paymentMethodBreakdown(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    long countByPaymentStatus(String paymentStatus);
}
