package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.entity.InsuranceClaim;

import java.util.List;

public interface InsuranceClaimService {

    List<InsuranceClaim> getAll();

    InsuranceClaim getById(Long id);

    List<InsuranceClaim> getPending();

    InsuranceClaim createClaim(InsuranceClaim claim);

    InsuranceClaim approveClaim(Long id, Double amount);

    InsuranceClaim rejectClaim(Long id, String reason);

    InsuranceClaim settleClaim(Long id);

    List<InsuranceClaim> getByInsuranceId(Long insuranceId);
}
