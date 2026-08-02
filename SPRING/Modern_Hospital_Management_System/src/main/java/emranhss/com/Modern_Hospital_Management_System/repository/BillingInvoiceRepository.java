package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.BillingInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillingInvoiceRepository extends JpaRepository<BillingInvoice, Long> {

    Optional<BillingInvoice> findByInvoiceNumber(String invoiceNumber);

    @Query("SELECT bi.invoiceNumber FROM BillingInvoice bi WHERE bi.invoiceNumber LIKE :prefix%")
    List<String> findInvoiceNumbersByPrefix(@Param("prefix") String prefix);

    List<BillingInvoice> findByPatientIdOrderByCreatedDateDesc(Long patientId);

    List<BillingInvoice> findByAdmittedPatientIdOrderByCreatedDateDesc(Long admittedPatientId);

    List<BillingInvoice> findByPaymentStatusOrderByCreatedDateDesc(String paymentStatus);

    List<BillingInvoice> findByInvoiceStatusOrderByCreatedDateDesc(String invoiceStatus);

    @Query("SELECT bi FROM BillingInvoice bi WHERE bi.createdDate BETWEEN :start AND :end ORDER BY bi.createdDate DESC")
    List<BillingInvoice> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT bi FROM BillingInvoice bi WHERE bi.invoiceNumber LIKE %:search% OR bi.patient.name LIKE %:search% ORDER BY bi.createdDate DESC")
    List<BillingInvoice> searchInvoices(@Param("search") String search);

    @Query("SELECT COUNT(bi) FROM BillingInvoice bi WHERE bi.createdDate BETWEEN :start AND :end")
    long countByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(bi.netAmount), 0) FROM BillingInvoice bi WHERE bi.createdDate BETWEEN :start AND :end")
    Double sumNetAmountByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(bi.totalPaid), 0) FROM BillingInvoice bi WHERE bi.createdDate BETWEEN :start AND :end")
    Double sumPaidAmountByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(bi.dueAmount), 0) FROM BillingInvoice bi WHERE bi.dueAmount > 0")
    Double sumTotalDue();

    long countByPaymentStatus(String paymentStatus);

    @Query("SELECT bi.paymentStatus, COUNT(bi) FROM BillingInvoice bi GROUP BY bi.paymentStatus")
    List<Object[]> countByPaymentStatusGrouped();
}
