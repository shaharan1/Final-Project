package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.PharmacySale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PharmacySaleRepository extends JpaRepository<PharmacySale, Long> {

    @Query("SELECT p FROM PharmacySale p WHERE p.saleDate BETWEEN :start AND :end ORDER BY p.saleDate DESC")
    List<PharmacySale> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(p.netPayable), 0) FROM PharmacySale p WHERE p.paymentStatus = 'PAID' AND p.saleDate BETWEEN :start AND :end")
    Double sumSalesByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(p.netPayable), 0) FROM PharmacySale p WHERE p.paymentStatus = 'PAID' AND p.saleDate BETWEEN :start AND :end")
    Double totalRevenue(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(p) FROM PharmacySale p WHERE p.paymentStatus = 'PAID' AND p.saleDate BETWEEN :start AND :end")
    long countSalesByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM((i.unitPrice - i.medicineStock.purchasePrice) * i.quantity), 0) " +
            "FROM PharmacySaleItem i JOIN i.pharmacySale p " +
            "WHERE p.paymentStatus = 'PAID' AND p.saleDate BETWEEN :start AND :end")
    Double sumProfitByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<PharmacySale> findByPaymentStatus(String status);

    List<PharmacySale> findAllByOrderBySaleDateDesc();
}
