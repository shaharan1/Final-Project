package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.TestAdmitedPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TestAdmitedPatientRepository extends JpaRepository<TestAdmitedPatient, Long> {
    List<TestAdmitedPatient> findByAdmittedPatientId(Long admittedPatientId);

    // Add this line in to facing problem
    List<TestAdmitedPatient> findByAdmittedPatientIdAndBillingStatus(Long admittedPatientId, String billingStatus);
}
