package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.SurgeryMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.mapper.SurgeryScheduleMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.BillingInvoiceRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.SurgeryRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.BillingInvoiceResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SurgeryResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SurgeryScheduleResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.*;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.*;
import emranhss.com.Modern_Hospital_Management_System.service.BillingInvoiceService;
import emranhss.com.Modern_Hospital_Management_System.service.SurgeryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurgeryServiceImp implements SurgeryService {

    private final SurgeryRepository surgeryRepository;
    private final PatientRepository patientRepository;
    private final AdmittedPatientRepository admittedPatientRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorDepartmentRepository doctorDepartmentRepository;
    private final SurgeryCategoryRepository surgeryCategoryRepository;
    private final SurgeryMasterRepository surgeryMasterRepository;
    private final OperationTheatreRepository operationTheatreRepository;
    private final DoctorDiscountRepository doctorDiscountRepository;
    private final BedBookingRepository bedBookingRepository;
    private final BillingInvoiceRepository billingInvoiceRepository;
    private final BillingInvoiceItemRepository billingInvoiceItemRepository;
    private final BillingInvoiceService billingInvoiceService;
    private final SurgeryMapper surgeryMapper;
    private final SurgeryScheduleMapper surgeryScheduleMapper;

    public SurgeryServiceImp(SurgeryRepository surgeryRepository,
                             PatientRepository patientRepository,
                             AdmittedPatientRepository admittedPatientRepository,
                             DoctorRepository doctorRepository,
                             DoctorDepartmentRepository doctorDepartmentRepository,
                             SurgeryCategoryRepository surgeryCategoryRepository,
                             SurgeryMasterRepository surgeryMasterRepository,
                             OperationTheatreRepository operationTheatreRepository,
                             DoctorDiscountRepository doctorDiscountRepository,
                             BedBookingRepository bedBookingRepository,
                             BillingInvoiceRepository billingInvoiceRepository,
                             BillingInvoiceItemRepository billingInvoiceItemRepository,
                             BillingInvoiceService billingInvoiceService,
                             SurgeryMapper surgeryMapper,
                             SurgeryScheduleMapper surgeryScheduleMapper) {
        this.surgeryRepository = surgeryRepository;
        this.patientRepository = patientRepository;
        this.admittedPatientRepository = admittedPatientRepository;
        this.doctorRepository = doctorRepository;
        this.doctorDepartmentRepository = doctorDepartmentRepository;
        this.surgeryCategoryRepository = surgeryCategoryRepository;
        this.surgeryMasterRepository = surgeryMasterRepository;
        this.operationTheatreRepository = operationTheatreRepository;
        this.doctorDiscountRepository = doctorDiscountRepository;
        this.bedBookingRepository = bedBookingRepository;
        this.billingInvoiceRepository = billingInvoiceRepository;
        this.billingInvoiceItemRepository = billingInvoiceItemRepository;
        this.billingInvoiceService = billingInvoiceService;
        this.surgeryMapper = surgeryMapper;
        this.surgeryScheduleMapper = surgeryScheduleMapper;
    }

    @Override
    @Transactional
    public SurgeryResponse create(SurgeryRequest request) {
        Patient patient = resolvePatient(request.getPatientId());
        AdmittedPatient admission = resolveAdmittedPatient(request.getAdmittedPatientId());
        Doctor surgeon = resolveDoctor(request.getSurgeonId(), "Surgeon");
        Doctor assistant = resolveDoctor(request.getAssistantSurgeonId(), "Assistant surgeon");
        Doctor anesthesiologist = resolveDoctor(request.getAnesthesiologistId(), "Anesthesiologist");
        DoctorDepartment department = resolveDepartment(request.getDepartmentId());
        SurgeryCategory category = resolveCategory(request.getCategoryId());
        SurgeryMaster master = resolveMaster(request.getSurgeryMasterId());
        OperationTheatre ot = resolveOperationTheatre(request.getOperationTheatreId());

        checkOtConflict(null, ot, request.getSurgeryDate(), request.getStartTime());

        Surgery surgery = surgeryMapper.toEntity(request, patient, admission, surgeon, assistant,
                anesthesiologist, department, category, master, ot, generateSurgeryNumber());
        applyTotals(surgery);
        surgery = surgeryRepository.save(surgery);

        integrateWithBilling(surgery);
        if (surgery.getBillingInvoiceId() != null) {
            surgery = surgeryRepository.save(surgery);
        }

        return toResponse(surgery);
    }

    @Override
    public SurgeryResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    public List<SurgeryResponse> getAll() {
        return surgeryRepository.findAllActiveOrderByDateDesc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SurgeryResponse> getByPatientId(Long patientId) {
        return surgeryRepository.findByPatientIdOrderBySurgeryDateDesc(patientId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SurgeryResponse> getByAdmittedPatientId(Long admittedPatientId) {
        return surgeryRepository.findByAdmittedPatientIdOrderBySurgeryDateDesc(admittedPatientId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SurgeryResponse> getByDateRange(LocalDate from, LocalDate to) {
        return surgeryRepository.findBySurgeryDateBetweenOrderByStartTimeAsc(from, to).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SurgeryResponse> getByOperationTheatreAndDate(Long operationTheatreId, LocalDate date) {
        return surgeryRepository.findByOperationTheatreIdAndSurgeryDate(operationTheatreId, date).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SurgeryResponse> search(String q) {
        return surgeryRepository.search(q).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SurgeryResponse update(Long id, SurgeryRequest request) {
        Surgery surgery = findOrThrow(id);
        Patient patient = resolvePatient(request.getPatientId());
        AdmittedPatient admission = resolveAdmittedPatient(request.getAdmittedPatientId());
        Doctor surgeon = resolveDoctor(request.getSurgeonId(), "Surgeon");
        Doctor assistant = resolveDoctor(request.getAssistantSurgeonId(), "Assistant surgeon");
        Doctor anesthesiologist = resolveDoctor(request.getAnesthesiologistId(), "Anesthesiologist");
        DoctorDepartment department = resolveDepartment(request.getDepartmentId());
        SurgeryCategory category = resolveCategory(request.getCategoryId());
        SurgeryMaster master = resolveMaster(request.getSurgeryMasterId());
        OperationTheatre ot = resolveOperationTheatre(request.getOperationTheatreId());

        checkOtConflict(id, ot, request.getSurgeryDate(), request.getStartTime());

        surgeryMapper.updateEntity(surgery, request, patient, admission, surgeon, assistant,
                anesthesiologist, department, category, master, ot);
        applyTotals(surgery);
        surgery = surgeryRepository.save(surgery);

        syncBillingItem(surgery);
        return toResponse(surgery);
    }

    @Override
    @Transactional
    public SurgeryResponse updateStatus(Long id, String status, String cancellationReason) {
        Surgery surgery = findOrThrow(id);
        if (status != null) {
            surgery.setStatus(status);
        }
        if (cancellationReason != null) {
            surgery.setCancellationReason(cancellationReason);
        }
        if ("CANCELLED".equals(status) && surgery.getBillingInvoiceId() != null) {
            List<BillingInvoiceItem> items = billingInvoiceItemRepository
                    .findBySourceModuleAndSourceId("SURGERY", surgery.getId());
            for (BillingInvoiceItem item : items) {
                if (!"CANCELLED".equals(item.getItemStatus())) {
                    item.setItemStatus("CANCELLED");
                    billingInvoiceItemRepository.save(item);
                }
            }
            surgery.setBillingStatus("CANCELLED");
        }
        return toResponse(surgeryRepository.save(surgery));
    }

    @Override
    public void delete(Long id) {
        Surgery surgery = findOrThrow(id);
        surgeryRepository.delete(surgery);
    }

    @Override
    public List<SurgeryScheduleResponse> getSchedule(LocalDate date) {
        LocalDate day = date != null ? date : LocalDate.now();
        return surgeryRepository.findBySurgeryDateOrderByStartTimeAsc(day).stream()
                .map(surgeryScheduleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SurgeryScheduleResponse> getUpcomingSchedule() {
        return surgeryRepository.findBySurgeryDateBetweenOrderByStartTimeAsc(
                        LocalDate.now(), LocalDate.now().plusDays(7)).stream()
                .filter(s -> !"CANCELLED".equals(s.getStatus()))
                .map(surgeryScheduleMapper::toResponse)
                .collect(Collectors.toList());
    }

    // ======================= INTERNAL HELPERS =======================

    private SurgeryResponse toResponse(Surgery surgery) {
        BedBooking booking = null;
        if (surgery.getAdmittedPatient() != null) {
            booking = bedBookingRepository
                    .findByAdmittedPatientIdAndActiveTrue(surgery.getAdmittedPatient().getId())
                    .orElse(null);
        }
        return surgeryMapper.toResponse(surgery, booking);
    }

    private void applyTotals(Surgery surgery) {
        double subtotal = surgery.getSubtotal() != null ? surgery.getSubtotal() : 0.0;

        double discountPct;
        if (surgery.getDiscountPercent() != null) {
            discountPct = surgery.getDiscountPercent();
        } else {
            discountPct = effectiveDiscountPercent(surgery.getSurgeon());
            surgery.setDiscountPercent(discountPct);
        }

        double discountAmount = round2(subtotal * discountPct / 100.0);
        double vatRate = surgery.getVatRate() != null ? surgery.getVatRate() : 0.0;
        double vatAmount = round2((subtotal - discountAmount) * vatRate / 100.0);
        double totalAmount = round2(subtotal - discountAmount + vatAmount);

        double insurance = surgery.getInsuranceCoverage() != null ? surgery.getInsuranceCoverage() : 0.0;
        double advance = surgery.getAdvancePaid() != null ? surgery.getAdvancePaid() : 0.0;
        double finalPayable = round2(Math.max(0.0, totalAmount - insurance - advance));

        surgery.setDiscountAmount(discountAmount);
        surgery.setVatRate(vatRate);
        surgery.setVatAmount(vatAmount);
        surgery.setInsuranceCoverage(insurance);
        surgery.setAdvancePaid(advance);
        surgery.setFinalPayable(finalPayable);
    }

    private double effectiveDiscountPercent(Doctor surgeon) {
        if (surgeon == null) {
            return 0.0;
        }
        return doctorDiscountRepository.findByDoctorIdAndActiveTrue(surgeon.getId())
                .map(d -> d.getEffectiveDiscountPercent() != null ? d.getEffectiveDiscountPercent() : 0.0)
                .orElse(0.0);
    }

    private void integrateWithBilling(Surgery surgery) {
        if (surgery.getFinalPayable() == null || surgery.getFinalPayable() <= 0
                || "CANCELLED".equals(surgery.getStatus())) {
            return;
        }
        if (surgery.getBillingInvoiceId() != null) {
            syncBillingItem(surgery);
            return;
        }

        BillingInvoice invoice = findOrCreateDraftInvoice(surgery);
        if (invoice == null) {
            return;
        }

        List<BillingInvoiceItem> existing = billingInvoiceItemRepository
                .findBySourceModuleAndSourceId("SURGERY", surgery.getId());
        if (existing.stream().noneMatch(i -> "ACTIVE".equals(i.getItemStatus()))) {
            BillingInvoiceRequest.BillingInvoiceItemRequest item = buildBillingItem(surgery);
            billingInvoiceService.addItem(invoice.getId(), item);
        }

        surgery.setBillingInvoiceId(invoice.getId());
        surgery.setBillingInvoiceNumber(invoice.getInvoiceNumber());
        surgery.setBillingStatus("DRAFT");
    }

    private void syncBillingItem(Surgery surgery) {
        if (surgery.getBillingInvoiceId() == null) {
            return;
        }
        List<BillingInvoiceItem> items = billingInvoiceItemRepository
                .findBySourceModuleAndSourceId("SURGERY", surgery.getId());
        if (items.isEmpty()) {
            integrateWithBilling(surgery);
            return;
        }
        boolean updated = false;
        for (BillingInvoiceItem item : items) {
            if ("ACTIVE".equals(item.getItemStatus())) {
                item.setDescription(buildItemDescription(surgery));
                item.setUnitPrice(surgery.getFinalPayable());
                item.calculateAmount();
                billingInvoiceItemRepository.save(item);
                updated = true;
            }
        }
        if (updated) {
            billingInvoiceRepository.findById(surgery.getBillingInvoiceId()).ifPresent(invoice -> {
                invoice.recalculateTotals();
                billingInvoiceRepository.save(invoice);
            });
        }
    }

    private BillingInvoiceRequest.BillingInvoiceItemRequest buildBillingItem(Surgery surgery) {
        BillingInvoiceRequest.BillingInvoiceItemRequest item = new BillingInvoiceRequest.BillingInvoiceItemRequest();
        item.setCategoryCode("SURGERY");
        item.setDescription(buildItemDescription(surgery));
        item.setQuantity(1);
        item.setUnitPrice(surgery.getFinalPayable());
        item.setDiscountPercent(0.0);
        item.setSourceModule("SURGERY");
        item.setSourceId(surgery.getId());
        return item;
    }

    private String buildItemDescription(Surgery surgery) {
        String name = surgery.getSurgeryMaster() != null ? surgery.getSurgeryMaster().getSurgeryName() : "Surgery";
        return "Surgery - " + name + " (" + surgery.getSurgeryNumber() + ")";
    }

    private BillingInvoice findOrCreateDraftInvoice(Surgery surgery) {
        Patient patient = surgery.getPatient();
        AdmittedPatient admission = surgery.getAdmittedPatient();

        List<BillingInvoice> existing;
        if (admission != null) {
            existing = billingInvoiceRepository.findByAdmittedPatientIdOrderByCreatedDateDesc(admission.getId());
        } else {
            existing = billingInvoiceRepository.findByPatientIdOrderByCreatedDateDesc(patient.getId());
        }
        BillingInvoice draft = existing.stream()
                .filter(i -> "DRAFT".equals(i.getInvoiceStatus()))
                .findFirst()
                .orElse(null);
        if (draft != null) {
            return draft;
        }

        BillingInvoiceRequest request = new BillingInvoiceRequest();
        request.setPatientId(patient.getId());
        request.setAdmittedPatientId(admission != null ? admission.getId() : null);
        request.setInvoiceType(admission != null ? "INPATIENT" : "OUTPATIENT");
        request.setTaxRate(0.0);
        request.setDiscountPercent(0.0);
        BillingInvoiceResponse response = billingInvoiceService.createInvoice(request);
        return billingInvoiceRepository.findById(response.getId()).orElse(null);
    }

    private void checkOtConflict(Long selfId, OperationTheatre ot, LocalDate surgeryDate, LocalTime startTime) {
        if (ot == null || surgeryDate == null) {
            return;
        }
        List<Surgery> booked = surgeryRepository.findByOperationTheatreIdAndSurgeryDate(ot.getId(), surgeryDate);
        boolean conflict = booked.stream()
                .anyMatch(s -> (selfId == null || !s.getId().equals(selfId))
                        && !"CANCELLED".equals(s.getStatus())
                        && !"POSTPONED".equals(s.getStatus())
                        && startTime != null && s.getStartTime() != null
                        && startTime.equals(s.getStartTime()));
        if (conflict) {
            throw new IllegalStateException(
                    "Operation Theatre '" + ot.getOtName() + "' is already booked at " + surgeryDate + " " + startTime);
        }
    }

    private String generateSurgeryNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "SURG-" + datePart + "-";
        long max = 0;
        for (String num : surgeryRepository.findSurgeryNumbersByPrefix(prefix)) {
            try {
                long seq = Long.parseLong(num.substring(prefix.length()));
                max = Math.max(max, seq);
            } catch (Exception ignored) {
            }
        }
        return prefix + String.format("%03d", max + 1);
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private Surgery findOrThrow(Long id) {
        return surgeryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Surgery not found with id: " + id));
    }

    private Patient resolvePatient(Long id) {
        if (id == null) {
            throw new ResourceNotFoundException("Patient is required");
        }
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    }

    private AdmittedPatient resolveAdmittedPatient(Long id) {
        if (id == null) {
            return null;
        }
        return admittedPatientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AdmittedPatient not found with id: " + id));
    }

    private Doctor resolveDoctor(Long id, String label) {
        if (id == null) {
            return null;
        }
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(label + " not found with id: " + id));
    }

    private DoctorDepartment resolveDepartment(Long id) {
        if (id == null) {
            return null;
        }
        return doctorDepartmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }

    private SurgeryCategory resolveCategory(Long id) {
        if (id == null) {
            return null;
        }
        return surgeryCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Surgery category not found with id: " + id));
    }

    private SurgeryMaster resolveMaster(Long id) {
        if (id == null) {
            return null;
        }
        return surgeryMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Surgery master not found with id: " + id));
    }

    private OperationTheatre resolveOperationTheatre(Long id) {
        if (id == null) {
            return null;
        }
        return operationTheatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Operation theatre not found with id: " + id));
    }
}
