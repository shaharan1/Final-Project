package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.DietAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DietAssignmentRepository extends JpaRepository<DietAssignment, Long> {
    List<DietAssignment> findByStatus(String status);
    List<DietAssignment> findByPatientId(Long patientId);
    List<DietAssignment> findByAdmittedPatientId(Long admittedPatientId);
    List<DietAssignment> findByDieticianId(Long dieticianId);
    List<DietAssignment> findByAssignedByDoctorId(Long doctorId);
    List<DietAssignment> findByStatusAndStartDateBetween(String status, LocalDate start, LocalDate end);
    long countByStatus(String status);
    long countByDietPlanId(Long dietPlanId);
    @Query("SELECT da FROM DietAssignment da WHERE da.status = 'ACTIVE' AND (da.endDate IS NULL OR da.endDate >= :today)")
    List<DietAssignment> findActiveAssignments(@Param("today") LocalDate today);
}
