package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    // 1. Find all reports belonging to a specific patient by their unique code
    List<Report> findByPatientPatientCode(String patientCode);

    // 2. Find all reports assigned/referred by a specific doctor
    List<Report> findByDoctorId(Long doctorId);

    // 3. Find a report by its unique laboratory Sample Tracking ID
    Optional<Report> findBySampleId(String sampleId);

    // 4. Custom optimized query to fetch a report and eagerly load patient and doctor data in one DB call
    @Query("SELECT r FROM Report r JOIN FETCH r.patient JOIN FETCH r.doctor WHERE r.id = :id")
    Optional<Report> findByIdWithDetails(@Param("id") Long id);
}
