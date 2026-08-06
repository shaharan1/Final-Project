package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.Surgery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SurgeryRepository extends JpaRepository<Surgery, Long> {

    List<Surgery> findByPatientIdOrderBySurgeryDateDesc(Long patientId);

    List<Surgery> findByAdmittedPatientIdOrderBySurgeryDateDesc(Long admittedPatientId);

    List<Surgery> findBySurgeonIdOrderBySurgeryDateDesc(Long surgeonId);

    List<Surgery> findByOperationTheatreIdAndSurgeryDate(Long operationTheatreId, LocalDate surgeryDate);

    List<Surgery> findBySurgeryDateBetweenOrderByStartTimeAsc(LocalDate from, LocalDate to);

    List<Surgery> findBySurgeryDateOrderByStartTimeAsc(LocalDate date);

    List<Surgery> findByStatusOrderBySurgeryDateDesc(String status);

    long countByStatus(String status);

    long countBySurgeryDate(LocalDate date);

    long countByStatusAndSurgeryDate(String status, LocalDate date);

    @Query("SELECT s FROM Surgery s WHERE s.status <> 'CANCELLED' ORDER BY s.surgeryDate DESC")
    List<Surgery> findAllActiveOrderByDateDesc();

    @Query("SELECT s FROM Surgery s WHERE " +
            "(LOWER(s.surgeryNumber) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(s.patient.name) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(s.patient.patientCode) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(s.surgeon.user.name) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(s.surgeryMaster.surgeryName) LIKE LOWER(CONCAT('%', :q, '%'))) " +
            "ORDER BY s.surgeryDate DESC")
    List<Surgery> search(@Param("q") String q);

    @Query("SELECT s.surgeryMaster.surgeryName, COUNT(s), SUM(s.finalPayable) FROM Surgery s " +
            "WHERE s.status = 'COMPLETED' GROUP BY s.surgeryMaster.surgeryName ORDER BY COUNT(s) DESC")
    List<Object[]> topSurgeries();

    @Query("SELECT s.surgeon.user.name, COUNT(s), SUM(s.finalPayable) FROM Surgery s " +
            "WHERE s.status = 'COMPLETED' GROUP BY s.surgeon.user.name ORDER BY COUNT(s) DESC")
    List<Object[]> topSurgeons();

    @Query("SELECT s.category.name, COUNT(s), SUM(s.finalPayable) FROM Surgery s " +
            "WHERE s.status = 'COMPLETED' GROUP BY s.category.name ORDER BY COUNT(s) DESC")
    List<Object[]> topCategories();

    @Query("SELECT s.department.departmentName, COUNT(s) FROM Surgery s " +
            "GROUP BY s.department.departmentName ORDER BY COUNT(s) DESC")
    List<Object[]> surgeriesByDepartment();

    @Query("SELECT s.status, COUNT(s) FROM Surgery s GROUP BY s.status")
    List<Object[]> countByStatusGroup();

    @Query("SELECT MONTH(s.surgeryDate), COUNT(s), COALESCE(SUM(s.finalPayable),0) FROM Surgery s " +
            "WHERE s.status = 'COMPLETED' AND YEAR(s.surgeryDate) = :year " +
            "GROUP BY MONTH(s.surgeryDate) ORDER BY MONTH(s.surgeryDate)")
    List<Object[]> monthlyStats(@Param("year") int year);

    @Query("SELECT s.operationTheatre.otName, COUNT(s), COALESCE(SUM(s.finalPayable),0) FROM Surgery s " +
            "WHERE s.status <> 'CANCELLED' GROUP BY s.operationTheatre.otName ORDER BY COUNT(s) DESC")
    List<Object[]> otUtilization();
}
