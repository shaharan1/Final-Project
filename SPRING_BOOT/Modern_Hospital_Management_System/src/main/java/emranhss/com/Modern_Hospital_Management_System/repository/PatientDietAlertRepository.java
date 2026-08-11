package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.PatientDietAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PatientDietAlertRepository extends JpaRepository<PatientDietAlert, Long> {
    List<PatientDietAlert> findByStatus(String status);
    List<PatientDietAlert> findByPatientId(Long patientId);
    List<PatientDietAlert> findByAdmittedPatientId(Long admittedPatientId);
    List<PatientDietAlert> findByAlertType(String alertType);
    List<PatientDietAlert> findBySeverity(String severity);
    List<PatientDietAlert> findByStatusOrderByCreatedAtDesc(String status);
    long countByStatus(String status);
    long countByAlertTypeAndStatus(String alertType, String status);
}
