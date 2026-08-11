package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyBilling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmergencyBillingRepository extends JpaRepository<EmergencyBilling, Long> {
    Optional<EmergencyBilling> findByEmergencyPatientId(Long emergencyPatientId);
    Optional<EmergencyBilling> findByBillNumber(String billNumber);
    List<EmergencyBilling> findByPaymentStatus(String paymentStatus);
    List<EmergencyBilling> findByStatus(String status);
}
