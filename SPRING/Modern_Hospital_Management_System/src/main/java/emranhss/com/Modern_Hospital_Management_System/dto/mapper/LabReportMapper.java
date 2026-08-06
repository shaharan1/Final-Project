package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.response.*;
import emranhss.com.Modern_Hospital_Management_System.entity.*;
import emranhss.com.Modern_Hospital_Management_System.enums.ReportStatus;

import java.time.LocalDate;
import java.time.Period;
import java.util.stream.Collectors;

public class LabReportMapper {

    private LabReportMapper() {}

    public static ReferenceRangeResponse toRangeResponse(ReferenceRange r) {
        ReferenceRangeResponse resp = new ReferenceRangeResponse();
        resp.setId(r.getId());
        resp.setTestParameterId(r.getTestParameter() != null ? r.getTestParameter().getId() : null);
        resp.setGenderScope(r.getGenderScope());
        resp.setMinAgeYears(r.getMinAgeYears());
        resp.setMaxAgeYears(r.getMaxAgeYears());
        resp.setMinValue(r.getMinValue());
        resp.setMaxValue(r.getMaxValue());
        resp.setCriticalLow(r.getCriticalLow());
        resp.setCriticalHigh(r.getCriticalHigh());
        resp.setDisplayRange(r.getDisplayRange());
        resp.setPriority(r.getPriority());
        resp.setActive(r.getActive());
        return resp;
    }

    public static InterpretationRuleResponse toRuleResponse(InterpretationRule rule) {
        InterpretationRuleResponse resp = new InterpretationRuleResponse();
        resp.setId(rule.getId());
        resp.setTestParameterId(rule.getTestParameter() != null ? rule.getTestParameter().getId() : null);
        resp.setParameterStatus(rule.getParameterStatus());
        resp.setValueMatch(rule.getValueMatch());
        resp.setInterpretationText(rule.getInterpretationText());
        resp.setDisplayOrder(rule.getDisplayOrder());
        resp.setActive(rule.getActive());
        return resp;
    }

    public static TestParameterResponse toParameterResponse(TestParameter p) {
        TestParameterResponse resp = new TestParameterResponse();
        resp.setId(p.getId());
        resp.setTestMasterId(p.getTestMaster() != null ? p.getTestMaster().getId() : null);
        resp.setParameterName(p.getParameterName());
        resp.setParameterCode(p.getParameterCode());
        resp.setUnit(p.getUnit());
        resp.setResultType(p.getResultType() != null ? p.getResultType().name() : "NUMERIC");
        resp.setAllowedValues(p.getAllowedValues());
        resp.setDisplayOrder(p.getDisplayOrder());
        resp.setDecimalPrecision(p.getDecimalPrecision());
        resp.setNormalText(p.getNormalText());
        resp.setActive(p.getActive());
        if (p.getReferenceRanges() != null) {
            resp.setReferenceRanges(p.getReferenceRanges().stream()
                    .map(LabReportMapper::toRangeResponse)
                    .collect(Collectors.toList()));
        }
        if (p.getInterpretationRules() != null) {
            resp.setInterpretationRules(p.getInterpretationRules().stream()
                    .map(LabReportMapper::toRuleResponse)
                    .collect(Collectors.toList()));
        }
        return resp;
    }

    public static TestMasterDetailResponse toMasterDetail(TestMaster tm) {
        TestMasterDetailResponse resp = new TestMasterDetailResponse();
        resp.setId(tm.getId());
        resp.setTestCode(tm.getTestCode());
        resp.setTestName(tm.getTestName());
        resp.setStandardPrice(tm.getStandardPrice());
        resp.setNormalRange(tm.getNormalRange());
        resp.setActive(tm.getActive());
        return resp;
    }

    public static LabReportResultResponse toResultResponse(LabReportResult r) {
        LabReportResultResponse resp = new LabReportResultResponse();
        resp.setId(r.getId());
        resp.setParameterId(r.getTestParameter() != null ? r.getTestParameter().getId() : null);
        resp.setParameterName(r.getParameterName());
        resp.setParameterCode(r.getParameterCode());
        resp.setUnit(r.getUnit());
        resp.setResultValue(r.getResultValue());
        resp.setStatus(r.getStatus() != null ? r.getStatus().name() : "PENDING");
        resp.setStatusLabel(r.getStatus() != null ? r.getStatus().name() : "PENDING");
        resp.setInterpretation(r.getInterpretation());
        resp.setAbnormal(r.getAbnormal());
        resp.setCritical(r.getCritical());
        resp.setDisplayOrder(r.getDisplayOrder());
        if (r.getReferenceRange() != null) {
            resp.setReferenceRangeDisplay(r.getReferenceRange().getDisplayRange());
        }
        return resp;
    }

    public static LabReportResponse toReportResponse(LabReport report) {
        LabReportResponse resp = new LabReportResponse();
        resp.setId(report.getId());
        resp.setReportNumber(report.getReportNumber());
        resp.setReportStatus(report.getReportStatus() != null ? report.getReportStatus().name() : "PENDING");
        resp.setStatusLabel(report.getReportStatus() != null ? report.getReportStatus().name() : "PENDING");
        resp.setFinalImpression(report.getFinalImpression());
        resp.setRecommendation(report.getRecommendation());
        resp.setSpecialistName(report.getSpecialistName());
        resp.setSpecialistDesignation(report.getSpecialistDesignation());
        resp.setSpecialistSignature(report.getSpecialistSignature());
        resp.setCreatedBy(report.getCreatedBy());
        resp.setCreatedDate(report.getCreatedDate());
        resp.setReportedDate(report.getReportedDate());

        if (report.getTestOrder() != null) {
            Tests t = report.getTestOrder();
            resp.setTestOrderId(t.getId());
            resp.setOrderStatus(t.getOrderStatus());
            if (t.getTestMaster() != null) {
                resp.setTestMasterId(t.getTestMaster().getId());
                resp.setTestCode(t.getTestMaster().getTestCode());
                resp.setTestName(t.getTestMaster().getTestName());
            }
            if (t.getPatient() != null) {
                Patient p = t.getPatient();
                resp.setPatientId(p.getId());
                resp.setPatientCode(p.getPatientCode());
                resp.setPatientName(p.getName());
                resp.setPatientGender(p.getGender());
                resp.setPatientPhone(p.getPhone());
                if (p.getDateOfBirth() != null) {
                    resp.setPatientAge(String.valueOf(Period.between(p.getDateOfBirth(), LocalDate.now()).getYears()));
                }
            }
            resp.setSampleType(t.getSampleType());
            resp.setSampleCollectedDate(t.getSampleCollectedDate());
            resp.setSampleReceivedDate(t.getSampleReceivedDate());
            if (t.getPrescribedBy() != null) {
                resp.setDoctorId(t.getPrescribedBy().getId());
                resp.setDoctorName(t.getPrescribedBy().getUser().getName());
                resp.setDoctorSpecialization(t.getPrescribedBy().getSpecialization());
            }
        }

        if (report.getResults() != null) {
            resp.setResults(report.getResults().stream()
                    .map(LabReportMapper::toResultResponse)
                    .collect(Collectors.toList()));
        }
        return resp;
    }

    public static String statusColor(ReportStatus status) {
        if (status == null) return "#6c757d";
        return switch (status) {
            case NORMAL -> "#198754";
            case ABNORMAL, NEEDS_DOCTOR_REVIEW -> "#fd7e14";
            case CRITICAL, DENGUE_POSITIVE -> "#dc3545";
            case READY -> "#0d6efd";
            default -> "#6c757d";
        };
    }
}
