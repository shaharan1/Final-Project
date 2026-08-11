package emranhss.com.Modern_Hospital_Management_System.serviceimp.lab;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.LabReportMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.InterpretPreviewRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.LabReportCreateRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.LabReportResultEntry;
import emranhss.com.Modern_Hospital_Management_System.dto.request.VerifyLabReportRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.*;
import emranhss.com.Modern_Hospital_Management_System.entity.*;
import emranhss.com.Modern_Hospital_Management_System.enums.ParameterStatus;
import emranhss.com.Modern_Hospital_Management_System.enums.ReportStatus;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.pdf.PathologyReportPdfGenerator;
import emranhss.com.Modern_Hospital_Management_System.repository.*;
import emranhss.com.Modern_Hospital_Management_System.service.lab.DoctorNotificationService;
import emranhss.com.Modern_Hospital_Management_System.service.lab.InterpretationResult;
import emranhss.com.Modern_Hospital_Management_System.service.lab.LabInterpretationEngine;
import emranhss.com.Modern_Hospital_Management_System.service.lab.LabReportService;
import emranhss.com.Modern_Hospital_Management_System.service.lab.LabRuleEngine;
import emranhss.com.Modern_Hospital_Management_System.service.lab.ReportAnalysis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabReportServiceImpl implements LabReportService {

    private final TestsRepository testsRepository;
    private final TestParameterRepository testParameterRepository;
    private final LabReportRepository labReportRepository;
    private final LabReportResultRepository labReportResultRepository;
    private final AbnormalAlertRepository abnormalAlertRepository;
    private final LabInterpretationEngine interpretationEngine;
    private final LabRuleEngine ruleEngine;
    private final DoctorNotificationService doctorNotificationService;

    @Override
    @Transactional
    public LabReportResponse createReport(LabReportCreateRequest request) {
        if (request.getTestOrderId() == null) {
            throw new IllegalArgumentException("testOrderId is required");
        }
        Tests test = testsRepository.findById(request.getTestOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Test order not found with ID: " + request.getTestOrderId()));

        if (test.getLabReportId() != null) {
            return getReportById(test.getLabReportId());
        }

        LabReport report = new LabReport();
        report.setTestOrder(test);
        report.setReportStatus(ReportStatus.PENDING);
        report.setCreatedBy(request.getEnteredBy());
        report.setCreatedDate(LocalDateTime.now());

        List<LabReportResult> results = new ArrayList<>();
        int order = 0;
        for (LabReportResultEntry entry : request.getResults()) {
            TestParameter parameter = testParameterRepository.findById(entry.getParameterId())
                    .orElseThrow(() -> new ResourceNotFoundException("Test parameter not found with ID: " + entry.getParameterId()));

            Patient patient = test.getPatient();
            String gender = patient != null ? patient.getGender() : null;
            Integer ageYears = null;
            if (patient != null && patient.getDateOfBirth() != null) {
                ageYears = Period.between(patient.getDateOfBirth(), LocalDate.now()).getYears();
            }

            ReferenceRange range = interpretationEngine.resolveRange(parameter, gender, ageYears);
            InterpretationResult interpretation = interpretationEngine.interpret(parameter, range, entry.getResultValue());

            LabReportResult result = new LabReportResult();
            result.setLabReport(report);
            result.setTestParameter(parameter);
            result.setReferenceRange(range);
            result.setParameterName(parameter.getParameterName());
            result.setParameterCode(parameter.getParameterCode());
            result.setUnit(parameter.getUnit());
            result.setResultValue(entry.getResultValue());
            result.setStatus(interpretation.getStatus());
            result.setInterpretation(interpretation.getInterpretation());
            result.setAbnormal(isAbnormal(interpretation.getStatus()));
            result.setCritical(isCritical(interpretation.getStatus()));
            result.setDisplayOrder(order++);
            results.add(result);
        }

        report.setResults(results);

        ReportAnalysis analysis = ruleEngine.analyze(report);
        report.setReportStatus(analysis.getReportStatus());
        report.setFinalImpression(analysis.getFinalImpression());
        report.setRecommendation(analysis.getRecommendation());

        LabReport saved = labReportRepository.save(report);
        saved.setReportNumber("LR-" + String.format("%06d", saved.getId()));
        saved = labReportRepository.save(saved);

        test.setLabReportId(saved.getId());
        test.setOrderStatus("RESULT_ENTERED");
        test.setResultValue(results.stream()
                .map(r -> r.getParameterName() + "=" + r.getResultValue())
                .collect(Collectors.joining(", ")));
        test.setResultEnteredBy(request.getEnteredBy());
        test.setResultEnteredDate(LocalDateTime.now());
        testsRepository.save(test);

        createAlerts(saved);

        return LabReportMapper.toReportResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LabReportResponse getReportById(Long id) {
        LabReport report = labReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab report not found with ID: " + id));
        return LabReportMapper.toReportResponse(report);
    }

    @Override
    @Transactional(readOnly = true)
    public LabReportResponse getReportByTestOrderId(Long testOrderId) {
        LabReport report = labReportRepository.findByTestOrderId(testOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("Lab report not found for test order: " + testOrderId));
        return LabReportMapper.toReportResponse(report);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabReportResponse> getReportsByPatient(Long patientId) {
        return labReportRepository.findByTestOrderPatientIdOrderByCreatedDateDesc(patientId).stream()
                .map(LabReportMapper::toReportResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabReportResponse> getAllReports() {
        return labReportRepository.findAllByOrderByCreatedDateDesc().stream()
                .map(LabReportMapper::toReportResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public InterpretPreviewResponse preview(InterpretPreviewRequest request) {
        TestParameter parameter = testParameterRepository.findById(request.getParameterId())
                .orElseThrow(() -> new ResourceNotFoundException("Test parameter not found with ID: " + request.getParameterId()));
        ReferenceRange range = interpretationEngine.resolveRange(parameter, request.getPatientGender(), request.getAgeYears());
        InterpretationResult interpretation = interpretationEngine.interpret(parameter, range, request.getResultValue());

        InterpretPreviewResponse resp = new InterpretPreviewResponse();
        resp.setParameterId(parameter.getId());
        resp.setResultValue(request.getResultValue());
        resp.setUnit(parameter.getUnit());
        resp.setStatus(interpretation.getStatus().name());
        resp.setStatusLabel(interpretation.getStatus().name());
        resp.setInterpretation(interpretation.getInterpretation());
        resp.setAbnormal(isAbnormal(interpretation.getStatus()));
        resp.setCritical(isCritical(interpretation.getStatus()));
        if (range != null) {
            resp.setReferenceRangeDisplay(range.getDisplayRange());
        } else if (parameter.getNormalText() != null) {
            resp.setReferenceRangeDisplay(parameter.getNormalText());
        }
        return resp;
    }

    @Override
    @Transactional
    public LabReportResponse verifyReport(Long id, VerifyLabReportRequest request) {
        LabReport report = labReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab report not found with ID: " + id));

        report.setSpecialistName(request.getSpecialistName());
        report.setSpecialistDesignation(request.getSpecialistDesignation());
        report.setSpecialistSignature(request.getSpecialistSignature());
        report.setReportedDate(LocalDateTime.now());
        LabReport saved = labReportRepository.save(report);

        Tests test = report.getTestOrder();
        if (test != null) {
            test.setOrderStatus("VERIFIED");
            test.setVerifiedBy(request.getSpecialistName());
            test.setVerifiedDate(LocalDateTime.now());
            test.setVerificationNotes(request.getVerificationNotes());
            testsRepository.save(test);
        }

        doctorNotificationService.notifyReportReady(saved);

        return LabReportMapper.toReportResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LabDashboardResponse getDashboard() {
        LabDashboardResponse resp = new LabDashboardResponse();
        resp.setTotalReports(labReportRepository.count());
        resp.setNormalReports(labReportRepository.countByReportStatus(ReportStatus.NORMAL));
        resp.setAbnormalReports(labReportRepository.countByReportStatus(ReportStatus.ABNORMAL)
                + labReportRepository.countByReportStatus(ReportStatus.NEEDS_DOCTOR_REVIEW));
        resp.setCriticalReports(labReportRepository.countByReportStatus(ReportStatus.CRITICAL));
        resp.setDenguePositive(labReportRepository.countByReportStatus(ReportStatus.DENGUE_POSITIVE));

        long total = resp.getTotalReports();
        long reported = labReportRepository.findAllByOrderByCreatedDateDesc().stream()
                .filter(r -> r.getReportedDate() != null)
                .count();
        resp.setPendingVerification(total - reported);
        resp.setReadyReports(reported);

        resp.setRecentReports(labReportRepository.findAllByOrderByCreatedDateDesc().stream()
                .limit(10)
                .map(LabReportMapper::toReportResponse)
                .collect(Collectors.toList()));

        resp.setCriticalAlerts(abnormalAlertRepository.findByResolvedFalseOrderByCreatedDateDesc().stream()
                .map(a -> "[" + a.getParameterName() + "] " + a.getResultValue() + " — " + a.getStatus() + " (" + a.getPatient().getName() + ")")
                .limit(10)
                .collect(Collectors.toList()));

        return resp;
    }

    @Override
    public byte[] generateReportPdf(Long id) {
        LabReport report = labReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab report not found with ID: " + id));
        try {
            return PathologyReportPdfGenerator.generate(report);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate report PDF: " + e.getMessage(), e);
        }
    }

    private void createAlerts(LabReport report) {
        Patient patient = report.getTestOrder() != null ? report.getTestOrder().getPatient() : null;
        for (LabReportResult result : report.getResults()) {
            if (!Boolean.TRUE.equals(result.getAbnormal())) continue;

            AbnormalAlert alert = new AbnormalAlert();
            alert.setPatient(patient);
            alert.setLabReportId(report.getId());
            alert.setParameterName(result.getParameterName());
            alert.setResultValue(result.getResultValue());
            alert.setStatus(String.valueOf(result.getStatus()));
            alert.setSeverity(Boolean.TRUE.equals(result.getCritical()) ? "CRITICAL" : "WARNING");
            alert.setResolved(false);
            abnormalAlertRepository.save(alert);
        }
    }

    private boolean isAbnormal(ParameterStatus status) {
        if (status == null) return false;
        return !(status == ParameterStatus.NORMAL
                || status == ParameterStatus.NEGATIVE
                || status == ParameterStatus.NON_REACTIVE
                || status == ParameterStatus.PENDING);
    }

    private boolean isCritical(ParameterStatus status) {
        return status == ParameterStatus.CRITICAL_LOW || status == ParameterStatus.CRITICAL_HIGH;
    }
}
