package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.entity.BillingInvoice;
import emranhss.com.Modern_Hospital_Management_System.entity.Refund;
import emranhss.com.Modern_Hospital_Management_System.enums.RefundStatus;
import emranhss.com.Modern_Hospital_Management_System.exception.BadRequestException;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.BillingInvoiceRepository;
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
    private final BillingInvoiceRepository billingInvoiceRepository;

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
        if (refund.getRefundAmount() == null || refund.getRefundAmount() <= 0) {
            throw new BadRequestException("Refund amount must be greater than zero");
        }
        if (refund.getOriginalAmount() == null || refund.getRefundAmount() > refund.getOriginalAmount()) {
            throw new BadRequestException("Refund amount cannot exceed the original paid amount");
        }

        List<RefundStatus> activeRefunds = List.of(RefundStatus.APPROVED, RefundStatus.PROCESSED);
        Double alreadyRefunded = refundRepository.sumRefundedByInvoice(refund.getInvoiceNumber(), activeRefunds);
        double already = alreadyRefunded != null ? alreadyRefunded : 0.0;
        double allowed = (refund.getOriginalAmount() != null ? refund.getOriginalAmount() : 0.0) - already;
        if (refund.getRefundAmount() > allowed + 0.01) {
            throw new BadRequestException("Total approved/processed refunds for invoice "
                    + refund.getInvoiceNumber() + " would exceed the original paid amount");
        }

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
        Refund saved = refundRepository.save(refund);

        if (saved.getInvoiceNumber() != null) {
            double refundAmount = saved.getRefundAmount();
            String invoiceNumber = saved.getInvoiceNumber();
            billingInvoiceRepository.findByInvoiceNumber(invoiceNumber).ifPresent(invoice -> {
                double newPaid = Math.max(0,
                        (invoice.getTotalPaid() != null ? invoice.getTotalPaid() : 0.0) - refundAmount);
                invoice.setTotalPaid(newPaid);
                invoice.recalculateTotals();
                if (newPaid <= 0.01) {
                    invoice.setPaymentStatus("REFUNDED");
                } else {
                    invoice.setPaymentStatus("PARTIAL");
                }
                billingInvoiceRepository.save(invoice);
            });
        }

        return saved;
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
