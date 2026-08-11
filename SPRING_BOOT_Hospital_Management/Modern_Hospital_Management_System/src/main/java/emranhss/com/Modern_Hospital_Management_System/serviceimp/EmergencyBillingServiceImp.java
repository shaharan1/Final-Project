package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.EmergencyBillingMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyBillingRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyBillingResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyBilling;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyPatient;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.EmergencyBillingRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.EmergencyPatientRepository;
import emranhss.com.Modern_Hospital_Management_System.service.EmergencyBillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmergencyBillingServiceImp implements EmergencyBillingService {

    private final EmergencyBillingRepository emergencyBillingRepository;
    private final EmergencyBillingMapper emergencyBillingMapper;
    private final EmergencyPatientRepository emergencyPatientRepository;

    @Override
    @Transactional
    public EmergencyBillingResponse create(EmergencyBillingRequest request) {
        EmergencyBilling billing = emergencyBillingMapper.toEntity(request);
        billing.setBillNumber(generateBillNumber());
        billing.setStatus("DRAFT");
        billing.setPaymentStatus("UNPAID");
        calculateTotal(billing);
        EmergencyBilling saved = emergencyBillingRepository.save(billing);
        return emergencyBillingMapper.toResponse(saved);
    }

    @Override
    public EmergencyBillingResponse getById(Long id) {
        EmergencyBilling billing = emergencyBillingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency billing not found with id: " + id));
        return emergencyBillingMapper.toResponse(billing);
    }

    @Override
    public EmergencyBillingResponse getByEmergencyPatientId(Long emergencyPatientId) {
        EmergencyBilling billing = emergencyBillingRepository.findByEmergencyPatientId(emergencyPatientId)
                .orElseThrow(() -> new ResourceNotFoundException("No billing found for emergency patient id: " + emergencyPatientId));
        return emergencyBillingMapper.toResponse(billing);
    }

    @Override
    public List<EmergencyBillingResponse> getAll() {
        return emergencyBillingRepository.findAll().stream()
                .map(emergencyBillingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmergencyBillingResponse generateBill(Long emergencyPatientId) {
        EmergencyPatient patient = emergencyPatientRepository.findById(emergencyPatientId)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency patient not found with id: " + emergencyPatientId));

        EmergencyBilling billing = new EmergencyBilling();
        billing.setEmergencyPatient(patient);
        billing.setBillNumber(generateBillNumber());
        billing.setRegistrationFee(0.0);
        billing.setConsultationFee(0.0);
        billing.setBedCharge(0.0);
        billing.setMedicineCharge(0.0);
        billing.setLabCharge(0.0);
        billing.setRadiologyCharge(0.0);
        billing.setProcedureCharge(0.0);
        billing.setOperationCharge(0.0);
        billing.setAmbulanceCharge(0.0);
        billing.setConsumablesCharge(0.0);
        billing.setDoctorFee(0.0);
        billing.setNursingCharge(0.0);
        billing.setOtherCharges(0.0);
        billing.setDiscountPercent(0.0);
        billing.setVatPercent(18.0);
        billing.setInsuranceCoverage(0.0);
        billing.setAdvancePaid(0.0);
        billing.setStatus("DRAFT");
        billing.setPaymentStatus("UNPAID");
        calculateTotal(billing);

        EmergencyBilling saved = emergencyBillingRepository.save(billing);
        return emergencyBillingMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public EmergencyBillingResponse updatePaymentStatus(Long id, String status) {
        EmergencyBilling billing = emergencyBillingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency billing not found with id: " + id));
        billing.setPaymentStatus(status);
        if ("PAID".equals(status)) {
            billing.setPaidAt(LocalDateTime.now());
        }
        return emergencyBillingMapper.toResponse(emergencyBillingRepository.save(billing));
    }

    @Override
    public List<EmergencyBillingResponse> getByStatus(String status) {
        return emergencyBillingRepository.findByStatus(status).stream()
                .map(emergencyBillingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Double getTodayRevenue() {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().atTime(LocalTime.MAX);
        return emergencyBillingRepository.findAll().stream()
                .filter(b -> b.getPaidAt() != null && b.getPaidAt().isAfter(todayStart) && b.getPaidAt().isBefore(todayEnd))
                .mapToDouble(b -> b.getGrandTotal() != null ? b.getGrandTotal() : 0.0)
                .sum();
    }

    private void calculateTotal(EmergencyBilling billing) {
        double subtotal = 0.0;
        subtotal += billing.getRegistrationFee() != null ? billing.getRegistrationFee() : 0.0;
        subtotal += billing.getConsultationFee() != null ? billing.getConsultationFee() : 0.0;
        subtotal += billing.getBedCharge() != null ? billing.getBedCharge() : 0.0;
        subtotal += billing.getMedicineCharge() != null ? billing.getMedicineCharge() : 0.0;
        subtotal += billing.getLabCharge() != null ? billing.getLabCharge() : 0.0;
        subtotal += billing.getRadiologyCharge() != null ? billing.getRadiologyCharge() : 0.0;
        subtotal += billing.getProcedureCharge() != null ? billing.getProcedureCharge() : 0.0;
        subtotal += billing.getOperationCharge() != null ? billing.getOperationCharge() : 0.0;
        subtotal += billing.getAmbulanceCharge() != null ? billing.getAmbulanceCharge() : 0.0;
        subtotal += billing.getConsumablesCharge() != null ? billing.getConsumablesCharge() : 0.0;
        subtotal += billing.getDoctorFee() != null ? billing.getDoctorFee() : 0.0;
        subtotal += billing.getNursingCharge() != null ? billing.getNursingCharge() : 0.0;
        subtotal += billing.getOtherCharges() != null ? billing.getOtherCharges() : 0.0;
        billing.setSubtotal(subtotal);

        double discountAmount = subtotal * (billing.getDiscountPercent() != null ? billing.getDiscountPercent() : 0.0) / 100.0;
        billing.setDiscountAmount(discountAmount);

        double afterDiscount = subtotal - discountAmount;
        double vatAmount = afterDiscount * (billing.getVatPercent() != null ? billing.getVatPercent() : 0.0) / 100.0;
        billing.setVatAmount(vatAmount);

        double grandTotal = afterDiscount + vatAmount;
        grandTotal -= billing.getInsuranceCoverage() != null ? billing.getInsuranceCoverage() : 0.0;
        grandTotal -= billing.getAdvancePaid() != null ? billing.getAdvancePaid() : 0.0;
        billing.setGrandTotal(grandTotal);

        double dueAmount = grandTotal;
        billing.setDueAmount(dueAmount > 0 ? dueAmount : 0.0);
    }

    private String generateBillNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));
        long count = emergencyBillingRepository.count() + 1;
        return "EBIL-" + datePart + "-" + String.format("%04d", count);
    }
}
