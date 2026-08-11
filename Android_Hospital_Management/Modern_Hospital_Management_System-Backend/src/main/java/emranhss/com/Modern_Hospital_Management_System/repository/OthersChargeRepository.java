package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.OthersCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OthersChargeRepository extends JpaRepository<OthersCharge, Long> {
    List<OthersCharge> findByAdmittedPatientId(Long admittedPatientId);

    // Add this line in to facing invoice  problem
    List<OthersCharge> findByAdmittedPatientIdAndBillingStatus(Long admittedPatientId, String billingStatus);
}
