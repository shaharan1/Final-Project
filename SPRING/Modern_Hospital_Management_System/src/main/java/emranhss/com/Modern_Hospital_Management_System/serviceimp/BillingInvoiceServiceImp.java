package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.request.BillingInvoiceRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.BillingPaymentRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.BillingDashboardSummaryResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.BillingInvoiceResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.BillingPaymentResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.mapper.BillingInvoiceMapper;
import emranhss.com.Modern_Hospital_Management_System.entity.*;
import emranhss.com.Modern_Hospital_Management_System.enums.PaymentMethod;
import emranhss.com.Modern_Hospital_Management_System.enums.PaymentStatus;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.pdf.InvoicePdfGenerator;
import emranhss.com.Modern_Hospital_Management_System.repository.*;
import emranhss.com.Modern_Hospital_Management_System.service.BillingAggregationService;
import emranhss.com.Modern_Hospital_Management_System.service.BillingInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillingInvoiceServiceImp implements BillingInvoiceService {

    private final BillingInvoiceRepository invoiceRepository;
    private final BillingInvoiceItemRepository itemRepository;
    private final BillingPaymentRepository paymentRepository;
    private final ChargeCategoryRepository chargeCategoryRepository;
    private final PatientRepository patientRepository;
    private final AdmittedPatientRepository admittedPatientRepository;
    private final DoctorRepository doctorRepository;
    private final BillingInvoiceMapper mapper;
    private final BillingAggregationService aggregationService;

    private String generateInvoiceNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
        String prefix = "INV-" + datePart + "-";
        long max = 999;
        for (String num : invoiceRepository.findInvoiceNumbersByPrefix(prefix)) {
            try {
                long seq = Long.parseLong(num.substring(prefix.length()));
                max = Math.max(max, seq);
            } catch (Exception ignored) {
            }
        }
        long next = max + 1;
        return prefix + String.format("%04d", next % 10000);
    }

    @Override
    @Transactional
    public BillingInvoiceResponse createInvoice(BillingInvoiceRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        if (request.getAdmittedPatientId() != null) {
            List<BillingInvoice> existing = invoiceRepository.findByAdmittedPatientIdOrderByCreatedDateDesc(request.getAdmittedPatientId());
            BillingInvoice draft = existing.stream()
                    .filter(inv -> "DRAFT".equals(inv.getInvoiceStatus()))
                    .findFirst()
                    .orElse(null);
            if (draft != null) {
                return mapper.toResponse(draft);
            }
        }

        BillingInvoice invoice = new BillingInvoice();
        invoice.setInvoiceNumber(generateInvoiceNumber());
        invoice.setPatient(patient);
        invoice.setInvoiceType(request.getInvoiceType() != null ? request.getInvoiceType() : "INPATIENT");
        invoice.setTaxRate(request.getTaxRate() != null ? request.getTaxRate() : 0.0);
        invoice.setDiscountPercent(request.getDiscountPercent() != null ? request.getDiscountPercent() : 0.0);
        invoice.setNotes(request.getNotes());
        invoice.setPreparedBy(request.getPreparedBy());
        invoice.setPaymentStatus("UNPAID");
        invoice.setInvoiceStatus("DRAFT");

        if (request.getAdmittedPatientId() != null) {
            AdmittedPatient admission = admittedPatientRepository.findById(request.getAdmittedPatientId()).orElse(null);
            invoice.setAdmittedPatient(admission);
        }

        if (request.getReferringDoctorId() != null) {
            Doctor doctor = doctorRepository.findById(request.getReferringDoctorId()).orElse(null);
            invoice.setReferringDoctor(doctor);
        }

        invoice.setItems(new ArrayList<>());
        invoice = invoiceRepository.save(invoice);

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            for (BillingInvoiceRequest.BillingInvoiceItemRequest itemReq : request.getItems()) {
                ChargeCategory cat = chargeCategoryRepository.findByCode(itemReq.getCategoryCode()).orElse(null);

                BillingInvoiceItem item = new BillingInvoiceItem();
                item.setInvoice(invoice);
                item.setChargeCategory(cat);
                item.setCategoryCode(itemReq.getCategoryCode());
                item.setDescription(itemReq.getDescription());
                item.setQuantity(itemReq.getQuantity() != null ? itemReq.getQuantity() : 1);
                item.setUnitPrice(itemReq.getUnitPrice() != null ? itemReq.getUnitPrice() : 0.0);
                item.setDiscountPercent(itemReq.getDiscountPercent() != null ? itemReq.getDiscountPercent() : 0.0);
                item.setSourceModule(itemReq.getSourceModule());
                item.setSourceId(itemReq.getSourceId());
                item.setItemStatus("ACTIVE");
                item.calculateAmount();
                itemRepository.save(item);
                invoice.getItems().add(item);
            }
        }

        invoice.recalculateTotals();
        invoice = invoiceRepository.save(invoice);
        return mapper.toResponse(invoice);
    }

    @Override
    public BillingInvoiceResponse getById(Long id) {
        BillingInvoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
        return mapper.toResponse(invoice);
    }

    @Override
    public BillingInvoiceResponse getByInvoiceNumber(String invoiceNumber) {
        BillingInvoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
        return mapper.toResponse(invoice);
    }

    @Override
    public List<BillingInvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillingInvoiceResponse> getByPatientId(Long patientId) {
        return invoiceRepository.findByPatientIdOrderByCreatedDateDesc(patientId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillingInvoiceResponse> getByAdmittedPatientId(Long admittedPatientId) {
        return invoiceRepository.findByAdmittedPatientIdOrderByCreatedDateDesc(admittedPatientId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillingInvoiceResponse> searchInvoices(String search) {
        return invoiceRepository.searchInvoices(search).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BillingInvoiceResponse updateInvoice(Long id, BillingInvoiceRequest request) {
        BillingInvoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        if ("FINALIZED".equals(invoice.getInvoiceStatus())) {
            throw new IllegalStateException("Cannot edit a finalized invoice");
        }

        if (request.getTaxRate() != null) invoice.setTaxRate(request.getTaxRate());
        if (request.getDiscountPercent() != null) invoice.setDiscountPercent(request.getDiscountPercent());
        if (request.getNotes() != null) invoice.setNotes(request.getNotes());
        if (request.getInvoiceType() != null) invoice.setInvoiceType(request.getInvoiceType());

        if (request.getItems() != null) {
            for (BillingInvoiceItem existing : invoice.getItems()) {
                existing.setItemStatus("CANCELLED");
                itemRepository.save(existing);
            }
            invoice.getItems().clear();

            for (BillingInvoiceRequest.BillingInvoiceItemRequest itemReq : request.getItems()) {
                ChargeCategory cat = chargeCategoryRepository.findByCode(itemReq.getCategoryCode()).orElse(null);

                BillingInvoiceItem item = new BillingInvoiceItem();
                item.setInvoice(invoice);
                item.setChargeCategory(cat);
                item.setCategoryCode(itemReq.getCategoryCode());
                item.setDescription(itemReq.getDescription());
                item.setQuantity(itemReq.getQuantity() != null ? itemReq.getQuantity() : 1);
                item.setUnitPrice(itemReq.getUnitPrice() != null ? itemReq.getUnitPrice() : 0.0);
                item.setDiscountPercent(itemReq.getDiscountPercent() != null ? itemReq.getDiscountPercent() : 0.0);
                item.setSourceModule(itemReq.getSourceModule() != null ? itemReq.getSourceModule() : "MANUAL");
                item.setSourceId(itemReq.getSourceId());
                item.setItemStatus("ACTIVE");
                item.calculateAmount();
                itemRepository.save(item);
                invoice.getItems().add(item);
            }
        }

        invoice.recalculateTotals();
        invoice = invoiceRepository.save(invoice);
        return mapper.toResponse(invoice);
    }

    @Override
    @Transactional
    public BillingInvoiceResponse addItem(Long invoiceId, BillingInvoiceRequest.BillingInvoiceItemRequest itemReq) {
        BillingInvoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        if ("FINALIZED".equals(invoice.getInvoiceStatus())) {
            throw new IllegalStateException("Cannot edit a finalized invoice");
        }

        ChargeCategory cat = chargeCategoryRepository.findByCode(itemReq.getCategoryCode()).orElse(null);

        BillingInvoiceItem item = new BillingInvoiceItem();
        item.setInvoice(invoice);
        item.setChargeCategory(cat);
        item.setCategoryCode(itemReq.getCategoryCode());
        item.setDescription(itemReq.getDescription());
        item.setQuantity(itemReq.getQuantity() != null ? itemReq.getQuantity() : 1);
        item.setUnitPrice(itemReq.getUnitPrice() != null ? itemReq.getUnitPrice() : 0.0);
        item.setDiscountPercent(itemReq.getDiscountPercent() != null ? itemReq.getDiscountPercent() : 0.0);
        item.setSourceModule(itemReq.getSourceModule() != null ? itemReq.getSourceModule() : "MANUAL");
        item.setSourceId(itemReq.getSourceId());
        item.setItemStatus("ACTIVE");
        item.calculateAmount();
        itemRepository.save(item);

        invoice.getItems().add(item);
        invoice.recalculateTotals();
        invoice = invoiceRepository.save(invoice);
        return mapper.toResponse(invoice);
    }

    @Override
    @Transactional
    public BillingInvoiceResponse removeItem(Long invoiceId, Long itemId) {
        BillingInvoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        if ("FINALIZED".equals(invoice.getInvoiceStatus())) {
            throw new IllegalStateException("Cannot edit a finalized invoice");
        }

        BillingInvoiceItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice item not found"));
        item.setItemStatus("CANCELLED");
        itemRepository.save(item);

        invoice.getItems().removeIf(i -> i.getId().equals(itemId));
        invoice.recalculateTotals();
        invoice = invoiceRepository.save(invoice);
        return mapper.toResponse(invoice);
    }

    @Override
    @Transactional
    public BillingInvoiceResponse finalizeInvoice(Long id, String finalizedBy) {
        BillingInvoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        if ("FINALIZED".equals(invoice.getInvoiceStatus())) {
            throw new IllegalStateException("Invoice is already finalized");
        }

        invoice.setInvoiceStatus("FINALIZED");
        invoice.setFinalizedBy(finalizedBy);
        invoice.setFinalizedDate(LocalDateTime.now());
        invoice.recalculateTotals();
        invoice = invoiceRepository.save(invoice);
        return mapper.toResponse(invoice);
    }

    @Override
    @Transactional
    public BillingInvoiceResponse cancelInvoice(Long id) {
        BillingInvoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        invoice.setInvoiceStatus("CANCELLED");
        invoice = invoiceRepository.save(invoice);
        return mapper.toResponse(invoice);
    }

    @Override
    @Transactional
    public BillingInvoiceResponse syncFromModules(Long admittedPatientId) {
        AdmittedPatient admission = admittedPatientRepository.findById(admittedPatientId)
                .orElseThrow(() -> new ResourceNotFoundException("Admission not found"));

        Patient patient = admission.getPatient();

        List<BillingInvoice> existingInvoices = invoiceRepository.findByAdmittedPatientIdOrderByCreatedDateDesc(admittedPatientId);
        BillingInvoice invoice;
        boolean isNew = false;

        if (!existingInvoices.isEmpty()) {
            invoice = existingInvoices.get(0);
            if ("FINALIZED".equals(invoice.getInvoiceStatus())) {
                return mapper.toResponse(invoice);
            }
            invoice.getItems().clear();
        } else {
            invoice = new BillingInvoice();
            invoice.setInvoiceNumber(generateInvoiceNumber());
            invoice.setPatient(patient);
            invoice.setAdmittedPatient(admission);
            invoice.setInvoiceType("INPATIENT");
            invoice.setTaxRate(0.0);
            invoice.setDiscountPercent(0.0);
            invoice.setItems(new ArrayList<>());
            invoice.setPaymentStatus("UNPAID");
            invoice.setInvoiceStatus("DRAFT");
            isNew = true;
        }

        List<BillingInvoiceItem> aggregated = aggregationService.aggregateAll(invoice, patient.getId(), admittedPatientId);
        invoice.getItems().addAll(aggregated);

        invoice.recalculateTotals();
        invoice = invoiceRepository.save(invoice);
        return mapper.toResponse(invoice);
    }

    @Override
    @Transactional
    public BillingPaymentResponse processPayment(BillingPaymentRequest request) {
        BillingInvoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        BillingPayment payment = new BillingPayment();
        payment.setInvoice(invoice);
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()));
        payment.setTransactionId(request.getTransactionId());
        payment.setCardLast4(request.getCardLast4());
        payment.setBankName(request.getBankName());
        payment.setMobileProvider(request.getMobileProvider());
        payment.setInsuranceCoverage(request.getInsuranceCoverage() != null ? request.getInsuranceCoverage() : 0.0);
        payment.setSelfPayAmount(request.getSelfPayAmount() != null ? request.getSelfPayAmount() : request.getAmount());
        payment.setNotes(request.getNotes());
        payment.setProcessedBy(request.getProcessedBy());
        payment.setPaymentStatus(PaymentStatus.COMPLETED);

        if (request.getInsuranceCompanyId() != null) {
            Insurance insurance = new Insurance();
            insurance.setId(request.getInsuranceCompanyId());
            payment.setInsuranceCompany(insurance);
        }

        payment = paymentRepository.save(payment);

        invoice.setTotalPaid((invoice.getTotalPaid() != null ? invoice.getTotalPaid() : 0.0) + request.getAmount());
        invoice.recalculateTotals();

        if (invoice.getDueAmount() <= 0) {
            invoice.setPaymentStatus("PAID");
        } else {
            invoice.setPaymentStatus("PARTIAL");
        }

        invoiceRepository.save(invoice);

        BillingPaymentResponse resp = new BillingPaymentResponse();
        resp.setId(payment.getId());
        resp.setInvoiceId(invoice.getId());
        resp.setInvoiceNumber(invoice.getInvoiceNumber());
        resp.setPatientName(invoice.getPatient() != null ? invoice.getPatient().getName() : "");
        resp.setAmount(payment.getAmount());
        resp.setPaymentMethod(payment.getPaymentMethod().name());
        resp.setTransactionId(payment.getTransactionId());
        resp.setPaymentStatus(payment.getPaymentStatus().name());
        resp.setNotes(payment.getNotes());
        resp.setProcessedBy(payment.getProcessedBy());
        resp.setPaymentDate(payment.getPaymentDate());
        return resp;
    }

    @Override
    public List<BillingPaymentResponse> getPaymentsByInvoiceId(Long invoiceId) {
        return paymentRepository.findByInvoiceIdOrderByPaymentDateDesc(invoiceId).stream()
                .map(p -> {
                    BillingPaymentResponse resp = new BillingPaymentResponse();
                    resp.setId(p.getId());
                    resp.setInvoiceId(p.getInvoice().getId());
                    resp.setInvoiceNumber(p.getInvoice().getInvoiceNumber());
                    resp.setPatientName(p.getInvoice().getPatient() != null ? p.getInvoice().getPatient().getName() : "");
                    resp.setAmount(p.getAmount());
                    resp.setPaymentMethod(p.getPaymentMethod().name());
                    resp.setTransactionId(p.getTransactionId());
                    resp.setPaymentStatus(p.getPaymentStatus().name());
                    resp.setNotes(p.getNotes());
                    resp.setProcessedBy(p.getProcessedBy());
                    resp.setPaymentDate(p.getPaymentDate());
                    return resp;
                })
                .collect(Collectors.toList());
    }

    @Override
    public BillingDashboardSummaryResponse getDashboardSummary() {
        BillingDashboardSummaryResponse summary = new BillingDashboardSummaryResponse();

        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().atTime(LocalTime.MAX);

        summary.setTotalInvoices(invoiceRepository.count());
        summary.setTodayInvoices(invoiceRepository.countByDateRange(todayStart, todayEnd));
        summary.setTotalRevenue(invoiceRepository.sumNetAmountByDateRange(todayStart.minusYears(10), todayEnd));
        summary.setTodayRevenue(invoiceRepository.sumNetAmountByDateRange(todayStart, todayEnd));
        summary.setTotalCollected(invoiceRepository.sumPaidAmountByDateRange(todayStart.minusYears(10), todayEnd));
        summary.setTotalDue(invoiceRepository.sumTotalDue());
        summary.setUnpaidCount(invoiceRepository.countByPaymentStatus("UNPAID"));
        summary.setPartialCount(invoiceRepository.countByPaymentStatus("PARTIAL"));
        summary.setPaidCount(invoiceRepository.countByPaymentStatus("PAID"));

        List<Object[]> categoryData = itemRepository.sumAmountByCategory();
        List<BillingDashboardSummaryResponse.CategoryRevenue> categoryRevenues = new ArrayList<>();
        for (Object[] row : categoryData) {
            BillingDashboardSummaryResponse.CategoryRevenue cr = new BillingDashboardSummaryResponse.CategoryRevenue();
            cr.setCategoryCode((String) row[0]);
            cr.setTotalAmount((Double) row[1]);
            chargeCategoryRepository.findByCode((String) row[0]).ifPresent(cat -> cr.setCategoryName(cat.getName()));
            categoryRevenues.add(cr);
        }
        summary.setRevenueByCategory(categoryRevenues);

        List<Object[]> methodData = paymentRepository.paymentMethodBreakdown(todayStart.minusYears(10), todayEnd);
        List<BillingDashboardSummaryResponse.PaymentMethodSummary> methodSummaries = new ArrayList<>();
        for (Object[] row : methodData) {
            BillingDashboardSummaryResponse.PaymentMethodSummary ms = new BillingDashboardSummaryResponse.PaymentMethodSummary();
            ms.setMethod(row[0] != null ? row[0].toString() : "UNKNOWN");
            ms.setCount((Long) row[1]);
            ms.setTotalAmount((Double) row[2]);
            methodSummaries.add(ms);
        }
        summary.setPaymentMethodBreakdown(methodSummaries);

        return summary;
    }

    @Override
    public byte[] generatePdf(Long id) {
        BillingInvoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
        try {
            return InvoicePdfGenerator.generate(invoice);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate invoice PDF", e);
        }
    }
}
