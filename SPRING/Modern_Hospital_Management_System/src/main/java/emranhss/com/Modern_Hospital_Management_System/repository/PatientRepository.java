package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Looks up a profile using the unique registration string (e.g., PAT-8F2B3A)
    Optional<Patient> findByPatientCode(String patientCode);

    // Automatically creates: SELECT * FROM patients WHERE phone = ?
    Optional<Patient> findByPhone(String phone);
    Patient findTopByOrderByIdDesc();
}
