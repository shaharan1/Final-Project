package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.BillingInvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingInvoiceItemRepository extends JpaRepository<BillingInvoiceItem, Long> {

    List<BillingInvoiceItem> findByInvoiceId(Long invoiceId);

    List<BillingInvoiceItem> findByInvoiceIdAndCategoryCode(Long invoiceId, String categoryCode);

    List<BillingInvoiceItem> findByCategoryCode(String categoryCode);

    List<BillingInvoiceItem> findBySourceModuleAndSourceId(String sourceModule, Long sourceId);

    @Query("SELECT bii.categoryCode, SUM(bii.amount) FROM BillingInvoiceItem bii GROUP BY bii.categoryCode")
    List<Object[]> sumAmountByCategory();

    @Query("SELECT bii FROM BillingInvoiceItem bii WHERE bii.invoice.id = :invoiceId AND bii.itemStatus = 'ACTIVE'")
    List<BillingInvoiceItem> findActiveByInvoiceId(@Param("invoiceId") Long invoiceId);
}
