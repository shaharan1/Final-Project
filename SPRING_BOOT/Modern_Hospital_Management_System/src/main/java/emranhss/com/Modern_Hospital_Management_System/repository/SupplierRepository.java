package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    List<Supplier> findByActiveTrue();

    @Query("SELECT s FROM Supplier s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.contactPerson) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Supplier> search(String keyword);

    @Query("SELECT s FROM Supplier s WHERE s.totalDue > 0")
    List<Supplier> findSuppliersWithDue();
}
