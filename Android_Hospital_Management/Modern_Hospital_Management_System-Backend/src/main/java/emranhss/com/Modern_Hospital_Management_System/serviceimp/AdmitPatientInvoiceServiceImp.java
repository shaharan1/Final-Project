package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.AdmitPatientInvoiceMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.AdmitPatientInvoiceRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.AdmitPatientInvoiceResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.*;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.*;
import emranhss.com.Modern_Hospital_Management_System.service.AdmitPatientInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AdmitPatientInvoiceServiceImp implements AdmitPatientInvoiceService {


    private final AdmitPatientInvoiceRepository invoiceRepository;
    private final AdmittedPatientRepository admittedPatientRepository;
    private final BedBookingRepository bedBookingRepository;
    private final DoctorChargeRepository doctorChargeRepository;
    private final TestAdmitedPatientRepository testAdmitedPatientRepository;
    private final OthersChargeRepository othersChargeRepository;
    private final AdmitPatientInvoiceMapper mapper;


    @Override
    @Transactional(readOnly = true)
    public AdmitPatientInvoiceResponse calculateDraftInvoice(Long admittedPatientId) {
        AdmittedPatient admission = admittedPatientRepository.findById(admittedPatientId)
                .orElseThrow(() -> new ResourceNotFoundException("Admission record missing with ID: " + admittedPatientId));

        AdmitPatientInvoice invoice = calculateBillAggregations(admission);
        return mapper.toResponse(invoice);
    }

    @Override
    @Transactional
    public AdmitPatientInvoiceResponse finalizeAndSaveInvoice(AdmitPatientInvoiceRequest request) {
        AdmittedPatient admission = admittedPatientRepository.findById(request.getAdmittedPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Admission record missing with ID: " + request.getAdmittedPatientId()));

        // Calculate up-to-date aggregates
        AdmitPatientInvoice invoice = calculateBillAggregations(admission);

        invoice.setInvoiceNumber("DINV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        invoice.setDiscount(request.getDiscount());
        invoice.setNetPayable(invoice.getTotalAmount() + invoice.getTax() - request.getDiscount());
        invoice.setPaidAmount(request.getPaidAmount());

        double due = invoice.getNetPayable() - request.getPaidAmount();
        invoice.setDueAmount(due <= 0 ? 0.0 : due);
        invoice.setPaymentStatus(due <= 0 ? "PAID" : request.getPaidAmount() > 0 ? "PARTIAL" : "UNPAID");
        invoice.setGeneratedBy(request.getGeneratedBy());

        // Update billing statuses across nested tables to prevent double billing
        updateChildBillingStatuses(request.getAdmittedPatientId());

        return mapper.toResponse(invoiceRepository.save(invoice));
    }

    private AdmitPatientInvoice calculateBillAggregations(AdmittedPatient admission) {
        AdmitPatientInvoice invoice = new AdmitPatientInvoice();
        invoice.setAdmittedPatient(admission);

        // 1. Compute Bed Charges based on active stay hours or days
        BedBooking activeBooking = bedBookingRepository.findByAdmittedPatientIdAndActiveTrue(admission.getId()).orElse(null);
        if (activeBooking != null && activeBooking.getBed() != null && activeBooking.getBed().getWard() != null) {
            long hours = Duration.between(activeBooking.getStartTime(), LocalDateTime.now()).toHours();
            long days = (hours / 24) + (hours % 24 > 0 ? 1 : 0); // Round up partial days
            if (days == 0) days = 1;
            invoice.setBedCharges(days * activeBooking.getBed().getWard().getBasePricePerDay());
        }

        // 2. Aggregate Doctor Rounding Charges
//        double doctorSum = doctorChargeRepository.findByAdmittedPatientIdAndBillingStatus(admission.getId(), "PENDING")
//                .stream().mapToDouble(DoctorCharge::getAmount).sum();
//        invoice.setDoctorCharges(doctorSum);

        double doctorSum = doctorChargeRepository
                .findByAdmittedPatientIdAndBillingStatus(admission.getId(), "PENDING")
                .stream()
                .mapToDouble(DoctorCharge::getAmount)
                .sum();

        // 3. Aggregate Inpatient Lab Testing Fees
        double testSum = testAdmitedPatientRepository.findByAdmittedPatientIdAndBillingStatus(admission.getId(), "PENDING")
                .stream().mapToDouble(TestAdmitedPatient::getBilledAmount).sum();
        invoice.setTestCharges(testSum);

        // 4. Aggregate Miscellaneous Others Charges
        double othersSum = othersChargeRepository.findByAdmittedPatientIdAndBillingStatus(admission.getId(), "PENDING")
                .stream().mapToDouble(OthersCharge::getAmount).sum();
        invoice.setOtherCharges(othersSum);

        // Placeholder zeros for modules not implemented yet
        invoice.setMedicineCharges(0.0);
        invoice.setMealCharges(0.0);

        // Grand Totals Calculations
        double subTotal = invoice.getBedCharges() + invoice.getDoctorCharges() + invoice.getTestCharges() +
                invoice.getMedicineCharges() + invoice.getMealCharges() + invoice.getOtherCharges();
        invoice.setTotalAmount(subTotal);
        invoice.setTax(subTotal * 0.05); // Standard 5% hospital tax logic
        invoice.setNetPayable(subTotal + invoice.getTax());
        invoice.setDueAmount(invoice.getNetPayable());
        invoice.setPaymentStatus("UNPAID");

        return invoice;
    }

    private void updateChildBillingStatuses(Long admissionId) {
        doctorChargeRepository.findByAdmittedPatientIdAndBillingStatus(admissionId, "PENDING")
                .forEach(c -> { c.setBillingStatus("BILLED"); doctorChargeRepository.save(c); });

        testAdmitedPatientRepository.findByAdmittedPatientIdAndBillingStatus(admissionId, "PENDING")
                .forEach(t -> { t.setBillingStatus("BILLED"); testAdmitedPatientRepository.save(t); });

        othersChargeRepository.findByAdmittedPatientIdAndBillingStatus(admissionId, "PENDING")
                .forEach(o -> { o.setBillingStatus("BILLED"); othersChargeRepository.save(o); });
    }
}
