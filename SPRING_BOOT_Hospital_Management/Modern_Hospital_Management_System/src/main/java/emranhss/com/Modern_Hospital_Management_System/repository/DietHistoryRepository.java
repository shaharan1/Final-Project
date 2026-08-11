package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.DietHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DietHistoryRepository extends JpaRepository<DietHistory, Long> {
    List<DietHistory> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    List<DietHistory> findByDietAssignmentIdOrderByCreatedAtDesc(Long dietAssignmentId);
    List<DietHistory> findByActionTypeOrderByCreatedAtDesc(String actionType);
    @Query("SELECT dh FROM DietHistory dh WHERE dh.patient.id = :patientId AND dh.createdAt BETWEEN :startDate AND :endDate ORDER BY dh.createdAt DESC")
    List<DietHistory> findByPatientIdAndDateRange(@Param("patientId") Long patientId, @Param("startDate") java.time.LocalDateTime startDate, @Param("endDate") java.time.LocalDateTime endDate);
}
