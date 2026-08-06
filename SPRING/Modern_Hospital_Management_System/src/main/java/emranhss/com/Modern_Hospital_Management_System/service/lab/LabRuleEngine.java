package emranhss.com.Modern_Hospital_Management_System.service.lab;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import emranhss.com.Modern_Hospital_Management_System.entity.LabReport;
import emranhss.com.Modern_Hospital_Management_System.entity.LabReportResult;
import emranhss.com.Modern_Hospital_Management_System.entity.LabRule;
import emranhss.com.Modern_Hospital_Management_System.enums.ParameterStatus;
import emranhss.com.Modern_Hospital_Management_System.enums.ReportStatus;
import emranhss.com.Modern_Hospital_Management_System.repository.LabRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class LabRuleEngine {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final LabRuleRepository labRuleRepository;

    /**
     * Computes overall report status, final impression and recommendation.
     * Rule evaluation order:
     *  1. any critical result -> CRITICAL
     *  2. matched dynamic rule (e.g. dengue) -> rule driven status/impression
     *  3. multiple abnormal -> NEEDS_DOCTOR_REVIEW
     *  4. any abnormal -> ABNORMAL
     *  5. otherwise -> NORMAL
     */
    public ReportAnalysis analyze(LabReport report) {
        ReportAnalysis analysis = new ReportAnalysis();

        long normal = 0, abnormal = 0, critical = 0;
        for (LabReportResult r : report.getResults()) {
            ParameterStatus s = r.getStatus();
            if (s == null) continue;
            if (s == ParameterStatus.NORMAL || s == ParameterStatus.NEGATIVE || s == ParameterStatus.NON_REACTIVE) {
                normal++;
            } else {
                abnormal++;
            }
            if (s == ParameterStatus.CRITICAL_LOW || s == ParameterStatus.CRITICAL_HIGH) {
                critical++;
            }
        }
        analysis.setNormalCount(normal);
        analysis.setAbnormalCount(abnormal);
        analysis.setCriticalCount(critical);

        if (critical > 0) {
            analysis.setReportStatus(ReportStatus.CRITICAL);
            analysis.setFinalImpression("One or more parameters are critically outside the reference range.");
            analysis.setRecommendation("Immediate physician review and repeat testing recommended.");
            return analysis;
        }

        LabRule matched = matchRule(report.getResults());
        if (matched != null) {
            analysis.setReportStatus(ReportStatus.DENGUE_POSITIVE);
            analysis.setFinalImpression(matched.getFinalImpression());
            analysis.setRecommendation(matched.getRecommendation());
            return analysis;
        }

        if (abnormal >= 2) {
            analysis.setReportStatus(ReportStatus.NEEDS_DOCTOR_REVIEW);
            analysis.setFinalImpression("Multiple abnormal parameters detected.");
            analysis.setRecommendation("Report requires physician review.");
            return analysis;
        }

        if (abnormal == 1) {
            analysis.setReportStatus(ReportStatus.ABNORMAL);
            analysis.setFinalImpression("One or more parameters are outside the reference range.");
            analysis.setRecommendation("Clinical correlation advised.");
            return analysis;
        }

        analysis.setReportStatus(ReportStatus.NORMAL);
        analysis.setFinalImpression("All parameters are within the reference range.");
        analysis.setRecommendation("No immediate clinical action required.");
        return analysis;
    }

    private LabRule matchRule(List<LabReportResult> results) {
        Map<String, LabReportResult> byCode = results.stream()
                .filter(r -> r.getParameterCode() != null)
                .collect(Collectors.toMap(
                        LabReportResult::getParameterCode,
                        Function.identity(),
                        (a, b) -> a));

        List<LabRule> rules = labRuleRepository.findByActiveTrueOrderByPriorityAsc();
        for (LabRule rule : rules) {
            try {
                List<RuleCondition> conditions = OBJECT_MAPPER.readValue(
                        rule.getConditions(), new TypeReference<List<RuleCondition>>() {});
                if (conditions.isEmpty()) continue;
                boolean allMatch = conditions.stream()
                        .allMatch(c -> matches(c, byCode.get(c.getParameterCode())));
                if (allMatch) {
                    return rule;
                }
            } catch (Exception e) {
                log.warn("Failed to evaluate lab rule [{}]: {}", rule.getRuleCode(), e.getMessage());
            }
        }
        return null;
    }

    private boolean matches(RuleCondition c, LabReportResult result) {
        if (c == null || result == null) return false;

        if (c.getStatus() != null && !c.getStatus().isBlank()) {
            return c.getStatus().equalsIgnoreCase(String.valueOf(result.getStatus()));
        }

        if (c.getValue() == null || result.getResultValue() == null) return false;

        String actual = result.getResultValue().trim();
        String expected = c.getValue().trim();
        String op = c.getOperator() != null ? c.getOperator().toUpperCase() : "EQ";

        switch (op) {
            case "EQ":
                return actual.equalsIgnoreCase(expected);
            case "NE":
                return !actual.equalsIgnoreCase(expected);
            case "CONTAINS":
                return actual.toLowerCase().contains(expected.toLowerCase());
            case "LT":
            case "LTE":
            case "GT":
            case "GTE":
                try {
                    double a = Double.parseDouble(actual);
                    double e = Double.parseDouble(expected);
                    return switch (op) {
                        case "LT" -> a < e;
                        case "LTE" -> a <= e;
                        case "GT" -> a > e;
                        default -> a >= e;
                    };
                } catch (NumberFormatException ex) {
                    return false;
                }
            default:
                return actual.equalsIgnoreCase(expected);
        }
    }

    @lombok.Data
    public static class RuleCondition {
        private String parameterCode;
        private String operator;
        private String value;
        private String status;
    }
}
