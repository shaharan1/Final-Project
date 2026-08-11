package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyLabOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyLabOrderRepository extends JpaRepository<EmergencyLabOrder, Long> {
    List<EmergencyLabOrder> findByEmergencyPatientId(Long emergencyPatientId);
    List<EmergencyLabOrder> findByStatus(String status);
    List<EmergencyLabOrder> findByIsCritical(Boolean isCritical);
    List<EmergencyLabOrder> findByEmergencyPatientIdAndStatus(Long emergencyPatientId, String status);
}
