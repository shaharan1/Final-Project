package emranhss.com.Modern_Hospital_Management_System.repository;


import emranhss.com.Modern_Hospital_Management_System.entity.AdmittedPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmittedPatientRepository extends JpaRepository<AdmittedPatient, Long> {

    boolean existsByPatientIdAndAdmissionStatus(Long patientId, String admissionStatus);
}
