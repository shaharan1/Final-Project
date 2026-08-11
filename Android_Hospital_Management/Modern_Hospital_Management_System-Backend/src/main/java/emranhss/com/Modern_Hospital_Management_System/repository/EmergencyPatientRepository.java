package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmergencyPatientRepository extends JpaRepository<EmergencyPatient, Long> {
    Optional<EmergencyPatient> findByEmergencyNumber(String emergencyNumber);
    List<EmergencyPatient> findByStatus(String status);
    List<EmergencyPatient> findByTriageLevel(Integer triageLevel);
    List<EmergencyPatient> findBySeverityLevel(String severityLevel);
    long countByStatus(String status);
    long countByTriageLevel(Integer triageLevel);
    
    @Query("SELECT e FROM EmergencyPatient e WHERE LOWER(e.emergencyNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(e.patientName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(e.phone) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<EmergencyPatient> searchByKeyword(@Param("keyword") String keyword);
    
    List<EmergencyPatient> findByArrivalTimeBetween(LocalDateTime start, LocalDateTime end);
    long countByArrivalTimeBetween(LocalDateTime start, LocalDateTime end);
    long countByStatusIn(List<String> statuses);
}
