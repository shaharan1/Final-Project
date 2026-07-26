package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.MedicineStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicineStockRepository extends JpaRepository<MedicineStock, Long> {

    List<MedicineStock> findByMedicineNameContainingIgnoreCase(String name);

    @Query("SELECT m FROM MedicineStock m WHERE m.active = true AND m.stockQuantity > 0 ORDER BY m.medicineName")
    List<MedicineStock> findAvailableStock();

    @Query("SELECT m FROM MedicineStock m WHERE m.active = true AND (m.stockQuantity - COALESCE(m.reservedQuantity,0) - COALESCE(m.damagedQuantity,0)) <= m.reorderLevel")
    List<MedicineStock> findLowStock();

    @Query("SELECT m FROM MedicineStock m WHERE m.active = true AND m.expiryDate < :today")
    List<MedicineStock> findExpired(@Param("today") LocalDate today);

    @Query("SELECT m FROM MedicineStock m WHERE m.active = true AND m.expiryDate BETWEEN :today AND :futureDate")
    List<MedicineStock> findExpiringSoon(@Param("today") LocalDate today, @Param("futureDate") LocalDate futureDate);

    @Query("SELECT m FROM MedicineStock m WHERE m.active = true AND m.genericName LIKE CONCAT('%', :generic, '%')")
    List<MedicineStock> findByGeneric(@Param("generic") String generic);

    @Query("SELECT m FROM MedicineStock m WHERE m.active = true AND m.batchNumber = :batch")
    List<MedicineStock> findByBatchNumber(@Param("batch") String batch);

    @Query("SELECT m FROM MedicineStock m WHERE m.active = true AND m.supplier.id = :supplierId")
    List<MedicineStock> findBySupplierId(@Param("supplierId") Long supplierId);

    @Query("SELECT m FROM MedicineStock m WHERE m.active = true AND m.barcode = :barcode")
    MedicineStock findByBarcode(@Param("barcode") String barcode);

    @Query("SELECT COUNT(m) FROM MedicineStock m WHERE m.active = true")
    long countActiveMedicines();

    @Query("SELECT COALESCE(SUM(m.stockQuantity - COALESCE(m.reservedQuantity,0) - COALESCE(m.damagedQuantity,0)), 0) FROM MedicineStock m WHERE m.active = true")
    long totalAvailableStock();
}
