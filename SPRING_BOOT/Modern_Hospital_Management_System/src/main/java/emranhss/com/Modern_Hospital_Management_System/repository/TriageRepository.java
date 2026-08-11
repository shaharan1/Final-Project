package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.Triage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TriageRepository extends JpaRepository<Triage, Long> {
    Optional<Triage> findByEmergencyPatientId(Long emergencyPatientId);
    List<Triage> findTop5ByOrderByAssessedAtDesc();
    List<Triage> findByTriageLevel(Integer triageLevel);
    long countByTriageLevel(Integer triageLevel);
    
    @Query("SELECT t.triageLevel, COUNT(t) FROM Triage t GROUP BY t.triageLevel")
    List<Object[]> getTriageDistribution();
}
