package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByPatientCode(String patientCode);
    Optional<Patient> findByPhone(String phone);
    Patient findTopByOrderByIdDesc();

    @Query("SELECT p FROM Patient p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.patientCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.phone) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Patient> search(@Param("keyword") String keyword);
}
