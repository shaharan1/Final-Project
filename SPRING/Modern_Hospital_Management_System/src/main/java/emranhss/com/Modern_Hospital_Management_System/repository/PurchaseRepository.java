package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    Purchase findByInvoiceNo(String invoiceNo);

    @Query("SELECT p FROM Purchase p WHERE p.purchaseDate BETWEEN :start AND :end ORDER BY p.purchaseDate DESC")
    List<Purchase> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(p.netAmount), 0) FROM Purchase p WHERE p.purchaseDate BETWEEN :start AND :end")
    Double sumPurchasesByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(p.dueAmount), 0) FROM Purchase p WHERE p.dueAmount > 0")
    Double totalPendingDues();

    @Query("SELECT p FROM Purchase p WHERE p.supplier.id = :supplierId ORDER BY p.purchaseDate DESC")
    List<Purchase> findBySupplierId(@Param("supplierId") Long supplierId);

    List<Purchase> findAllByOrderByPurchaseDateDesc();
}
