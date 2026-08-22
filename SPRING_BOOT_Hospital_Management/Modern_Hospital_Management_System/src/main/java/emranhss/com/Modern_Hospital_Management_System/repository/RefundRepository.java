package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.Refund;
import emranhss.com.Modern_Hospital_Management_System.enums.RefundStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {

    List<Refund> findByRefundStatus(RefundStatus status);

    List<Refund> findByInvoiceNumber(String invoiceNumber);

    List<Refund> findByPatientId(Long patientId);

    @Query("SELECT COALESCE(SUM(r.refundAmount), 0.0) FROM Refund r " +
            "WHERE r.invoiceNumber = :invoiceNumber AND r.refundStatus IN :statuses")
    Double sumRefundedByInvoice(@Param("invoiceNumber") String invoiceNumber,
                                @Param("statuses") List<RefundStatus> statuses);

    @Query("SELECT SUM(r.refundAmount) FROM Refund r WHERE r.createdDate BETWEEN :start AND :end AND r.refundStatus = :status")
    Double sumRefundsByDateRangeAndStatus(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("status") RefundStatus status);
}
