package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.entity.Refund;
import emranhss.com.Modern_Hospital_Management_System.enums.RefundStatus;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.RefundRepository;
import emranhss.com.Modern_Hospital_Management_System.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefundServiceImp implements RefundService {

    private final RefundRepository refundRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Refund> getAll() {
        return refundRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Refund getById(Long id) {
        return refundRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Refund not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Refund> getPending() {
        return refundRepository.findByRefundStatus(RefundStatus.PENDING);
    }

    @Override
    @Transactional
    public Refund createRefund(Refund refund) {
        refund.setRefundReference(generateRefundReference());
        refund.setRefundStatus(RefundStatus.PENDING);
        if (refund.getCreatedDate() == null) {
            refund.setCreatedDate(LocalDateTime.now());
        }
        return refundRepository.save(refund);
    }

    @Override
    @Transactional
    public Refund approveRefund(Long id, String approvedBy) {
        Refund refund = refundRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Refund not found with ID: " + id));
        if (refund.getRefundStatus() != RefundStatus.PENDING) {
            throw new RuntimeException("Only pending refunds can be approved");
        }
        refund.setRefundStatus(RefundStatus.APPROVED);
        refund.setApprovedBy(approvedBy);
        return refundRepository.save(refund);
    }

    @Override
    @Transactional
    public Refund rejectRefund(Long id, String reason) {
        Refund refund = refundRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Refund not found with ID: " + id));
        if (refund.getRefundStatus() != RefundStatus.PENDING) {
            throw new RuntimeException("Only pending refunds can be rejected");
        }
        refund.setRefundStatus(RefundStatus.REJECTED);
        refund.setRejectionReason(reason);
        return refundRepository.save(refund);
    }

    @Override
    @Transactional
    public Refund processRefund(Long id) {
        Refund refund = refundRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Refund not found with ID: " + id));
        if (refund.getRefundStatus() != RefundStatus.APPROVED) {
            throw new RuntimeException("Only approved refunds can be processed");
        }
        refund.setRefundStatus(RefundStatus.PROCESSED);
        refund.setProcessedDate(LocalDateTime.now());
        return refundRepository.save(refund);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Refund> getByStatus(RefundStatus status) {
        return refundRepository.findByRefundStatus(status);
    }

    private String generateRefundReference() {
        long year = Year.now().getValue();
        long count = refundRepository.count() + 1;
        return String.format("REF-%d-%04d", year, count);
    }
}
