package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyBed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmergencyBedRepository extends JpaRepository<EmergencyBed, Long> {
    List<EmergencyBed> findByStatus(String status);
    List<EmergencyBed> findByWardName(String wardName);
    List<EmergencyBed> findByWardNameAndStatus(String wardName, String status);
    long countByStatus(String status);
    Optional<EmergencyBed> findByEmergencyPatientId(Long emergencyPatientId);
    
    @Query("SELECT b.wardName, b.status, COUNT(b) FROM EmergencyBed b GROUP BY b.wardName, b.status")
    List<Object[]> getWardSummary();
}
