package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.InsuranceClaim;
import emranhss.com.Modern_Hospital_Management_System.enums.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsuranceClaimRepository extends JpaRepository<InsuranceClaim, Long> {

    List<InsuranceClaim> findByClaimStatus(ClaimStatus status);

    List<InsuranceClaim> findByInsuranceId(Long insuranceId);

    List<InsuranceClaim> findByPatientId(Long patientId);

    @Query("SELECT ic.claimStatus as status, SUM(ic.claimAmount) as total FROM InsuranceClaim ic GROUP BY ic.claimStatus")
    List<Object[]> sumClaimsByStatus();
}
