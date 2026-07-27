package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.entity.InsuranceClaim;
import emranhss.com.Modern_Hospital_Management_System.enums.ClaimStatus;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.InsuranceClaimRepository;
import emranhss.com.Modern_Hospital_Management_System.service.InsuranceClaimService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InsuranceClaimServiceImp implements InsuranceClaimService {

    private final InsuranceClaimRepository insuranceClaimRepository;

    @Override
    @Transactional(readOnly = true)
    public List<InsuranceClaim> getAll() {
        return insuranceClaimRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public InsuranceClaim getById(Long id) {
        return insuranceClaimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance claim not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsuranceClaim> getPending() {
        return insuranceClaimRepository.findByClaimStatus(ClaimStatus.SUBMITTED);
    }

    @Override
    @Transactional
    public InsuranceClaim createClaim(InsuranceClaim claim) {
        String ref = generateClaimReference();
        claim.setClaimReference(ref);
        claim.setClaimNumber(ref);
        claim.setClaimStatus(ClaimStatus.SUBMITTED);
        if (claim.getCreatedDate() == null) {
            claim.setCreatedDate(LocalDateTime.now());
        }
        if (claim.getSubmissionDate() == null) {
            claim.setSubmissionDate(LocalDateTime.now());
        }
        return insuranceClaimRepository.save(claim);
    }

    @Override
    @Transactional
    public InsuranceClaim approveClaim(Long id, Double amount) {
        InsuranceClaim claim = insuranceClaimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance claim not found with ID: " + id));
        if (claim.getClaimStatus() != ClaimStatus.SUBMITTED && claim.getClaimStatus() != ClaimStatus.UNDER_REVIEW) {
            throw new RuntimeException("Only submitted or under review claims can be approved");
        }
        claim.setClaimStatus(amount >= claim.getClaimAmount() ? ClaimStatus.APPROVED : ClaimStatus.PARTIALLY_APPROVED);
        claim.setApprovedAmount(amount);
        claim.setReviewDate(LocalDateTime.now());
        return insuranceClaimRepository.save(claim);
    }

    @Override
    @Transactional
    public InsuranceClaim rejectClaim(Long id, String reason) {
        InsuranceClaim claim = insuranceClaimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance claim not found with ID: " + id));
        if (claim.getClaimStatus() != ClaimStatus.SUBMITTED && claim.getClaimStatus() != ClaimStatus.UNDER_REVIEW) {
            throw new RuntimeException("Only submitted or under review claims can be rejected");
        }
        claim.setClaimStatus(ClaimStatus.REJECTED);
        claim.setRejectionReason(reason);
        claim.setReviewDate(LocalDateTime.now());
        return insuranceClaimRepository.save(claim);
    }

    @Override
    @Transactional
    public InsuranceClaim settleClaim(Long id) {
        InsuranceClaim claim = insuranceClaimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance claim not found with ID: " + id));
        if (claim.getClaimStatus() != ClaimStatus.APPROVED && claim.getClaimStatus() != ClaimStatus.PARTIALLY_APPROVED) {
            throw new RuntimeException("Only approved claims can be settled");
        }
        claim.setClaimStatus(ClaimStatus.SETTLED);
        claim.setPaidAmount(claim.getApprovedAmount());
        claim.setSettlementDate(LocalDateTime.now());
        return insuranceClaimRepository.save(claim);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsuranceClaim> getByInsuranceId(Long insuranceId) {
        return insuranceClaimRepository.findByInsuranceId(insuranceId);
    }

    private String generateClaimReference() {
        long year = Year.now().getValue();
        long count = insuranceClaimRepository.count() + 1;
        return String.format("CLM-%d-%04d", year, count);
    }
}
