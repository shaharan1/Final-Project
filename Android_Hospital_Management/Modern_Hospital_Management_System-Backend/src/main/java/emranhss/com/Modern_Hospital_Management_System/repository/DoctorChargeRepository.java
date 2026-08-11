package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.DoctorCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DoctorChargeRepository extends JpaRepository<DoctorCharge, Long> {
    List<DoctorCharge> findByAdmittedPatientId(Long admittedPatientId);

    // Add this line in to facing problem
    List<DoctorCharge> findByAdmittedPatientIdAndBillingStatus(Long admittedPatientId, String billingStatus);
}
