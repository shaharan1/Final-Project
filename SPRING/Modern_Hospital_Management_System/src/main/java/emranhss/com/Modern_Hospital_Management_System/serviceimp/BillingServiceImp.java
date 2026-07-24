package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.BillingMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.response.BillingResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.*;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.*;
import emranhss.com.Modern_Hospital_Management_System.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BillingServiceImp implements BillingService {

    private final BillingRepository billingRepository;
    private final AdmittedPatientRepository admittedPatientRepository;
    private final BedBookingRepository bedBookingRepository;
    private final DoctorChargeRepository doctorChargeRepository;
    private final TestAdmitedPatientRepository testAdmitedPatientRepository;
    private final OthersChargeRepository othersChargeRepository;
    private final BillingMapper mapper;

    @Override
    @Transactional
    public BillingResponse syncAndGetInpatientBilling(Long admittedPatientId) {
        AdmittedPatient admission = admittedPatientRepository.findById(admittedPatientId)
                .orElseThrow(() -> new ResourceNotFoundException("Admission ledger missing with ID: " + admittedPatientId));

        // Find existing aggregate summary record or initialize a new blank one
        Billing billing = billingRepository.findByAdmittedPatientId(admittedPatientId)
                .orElseGet(() -> {
                    Billing b = new Billing();
                    b.setAdmittedPatient(admission);
                    return b;
                });

        if (billing.getBillingStatus().equals("CLOSED")) {
            return mapper.toResponse(billing);
        }

        // 1. Sync Ward Stay Fees
        BedBooking activeBooking = bedBookingRepository.findByAdmittedPatientIdAndActiveTrue(admittedPatientId).orElse(null);
        if (activeBooking != null && activeBooking.getBed() != null && activeBooking.getBed().getWard() != null) {
            long hours = Duration.between(activeBooking.getStartTime(), LocalDateTime.now()).toHours();
            long days = (hours / 24) + (hours % 24 > 0 ? 1 : 0);
            if (days == 0) days = 1;
            billing.setWardCost(days * activeBooking.getBed().getWard().getBasePricePerDay());
        }

        // 2. Sync Doctor Round Fees
        double docSum = doctorChargeRepository.findByAdmittedPatientIdAndBillingStatus(admittedPatientId, "PENDING")
                .stream().mapToDouble(DoctorCharge::getAmount).sum();
        billing.setDoctorCharge(docSum);

        // 3. Sync Lab Diagnostic Test Fees
        double testSum = testAdmitedPatientRepository.findByAdmittedPatientIdAndBillingStatus(admittedPatientId, "PENDING")
                .stream().mapToDouble(TestAdmitedPatient::getBilledAmount).sum();
        billing.setTestCost(testSum);

        // 4. Sync Miscellaneous Fees
        double othersSum = othersChargeRepository.findByAdmittedPatientIdAndBillingStatus(admittedPatientId, "PENDING")
                .stream().mapToDouble(OthersCharge::getAmount).sum();
        billing.setOtherCharge(othersSum);

        // Recalculate Grand Subtotal inside entity layer before persisting changes
        billing.calculateTotalCost();
        return mapper.toResponse(billingRepository.save(billing));
    }

    @Override
    @Transactional
    public BillingResponse closeBillingLedger(Long admittedPatientId) {
        Billing billing = billingRepository.findByAdmittedPatientId(admittedPatientId)
                .orElseThrow(() -> new ResourceNotFoundException("Billing ledger summary missing with ID: " + admittedPatientId));
        billing.setBillingStatus("CLOSED");
        return mapper.toResponse(billingRepository.save(billing));
    }
}
