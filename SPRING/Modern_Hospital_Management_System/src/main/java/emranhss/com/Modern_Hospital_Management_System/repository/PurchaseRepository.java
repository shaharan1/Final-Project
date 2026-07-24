package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    Optional<Purchase> findByInvoiceNo(String invoiceNo);
}
