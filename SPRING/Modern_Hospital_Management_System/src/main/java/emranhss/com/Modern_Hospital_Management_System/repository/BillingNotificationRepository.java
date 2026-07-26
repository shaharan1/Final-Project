package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.BillingNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingNotificationRepository extends JpaRepository<BillingNotification, Long> {

    List<BillingNotification> findByIsRead(boolean isRead);

    List<BillingNotification> findByType(String type);

    Long countByIsRead(boolean isRead);
}
