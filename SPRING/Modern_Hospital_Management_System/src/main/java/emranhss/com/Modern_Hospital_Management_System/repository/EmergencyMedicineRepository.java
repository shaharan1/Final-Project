package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyMedicineRepository extends JpaRepository<EmergencyMedicine, Long> {
    List<EmergencyMedicine> findByEmergencyPatientId(Long emergencyPatientId);
    List<EmergencyMedicine> findByStatus(String status);
    List<EmergencyMedicine> findByEmergencyPatientIdAndStatus(Long emergencyPatientId, String status);
}
