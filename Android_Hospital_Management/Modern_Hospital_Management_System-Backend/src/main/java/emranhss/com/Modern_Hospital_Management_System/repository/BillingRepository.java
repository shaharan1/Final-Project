package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {
    Optional<Billing> findByAdmittedPatientId(Long admittedPatientId);
}
