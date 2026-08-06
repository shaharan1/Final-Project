package emranhss.com.Modern_Hospital_Management_System.service.lab;

import emranhss.com.Modern_Hospital_Management_System.entity.InterpretationRule;
import emranhss.com.Modern_Hospital_Management_System.entity.ReferenceRange;
import emranhss.com.Modern_Hospital_Management_System.entity.TestParameter;
import emranhss.com.Modern_Hospital_Management_System.enums.ParameterStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class LabInterpretationEngine {

    /**
     * Resolves the best matching reference range for a parameter given patient gender and age.
     * All rules are DB-driven (gender scope, age brackets, priority).
     */
    public ReferenceRange resolveRange(TestParameter parameter, String gender, Integer ageYears) {
        if (parameter == null || parameter.getReferenceRanges() == null) {
            return null;
        }

        ReferenceRange best = null;
        int bestScore = Integer.MIN_VALUE;

        for (ReferenceRange r : parameter.getReferenceRanges()) {
            if (!Boolean.TRUE.equals(r.getActive())) continue;

            int score = 0;

            String scope = r.getGenderScope();
            if (scope == null || scope.isBlank() || scope.equalsIgnoreCase("ANY")) {
                score += 1;
            } else if (gender != null && scope.equalsIgnoreCase(gender)) {
                score += 10;
            } else {
                continue;
            }

            if (ageYears != null) {
                boolean ageInRange = (r.getMinAgeYears() == null || ageYears >= r.getMinAgeYears())
                        && (r.getMaxAgeYears() == null || ageYears <= r.getMaxAgeYears());
                if (ageInRange) {
                    score += 5;
                } else if (r.getMinAgeYears() != null || r.getMaxAgeYears() != null) {
                    continue;
                }
            } else if (r.getMinAgeYears() != null || r.getMaxAgeYears() != null) {
                continue;
            }

            score += (r.getPriority() != null ? r.getPriority() : 0) * 100;

            if (score > bestScore) {
                bestScore = score;
                best = r;
            }
        }
        return best;
    }

    /**
     * Compares the result value against the reference range and generates status + interpretation.
     */
    public InterpretationResult interpret(TestParameter parameter, ReferenceRange range, String value) {
        InterpretationResult result = new InterpretationResult();
        result.setReferenceRange(range);

        if (value == null || value.isBlank()) {
            result.setStatus(ParameterStatus.PENDING);
            result.setInterpretation("Result not entered yet.");
            return result;
        }

        String trimmed = value.trim();
        ParameterStatus status;
        String interpretation;

        switch (parameter.getResultType()) {
            case NUMERIC:
                status = interpretNumeric(parameter, range, trimmed);
                interpretation = interpretationText(parameter, status, null, trimmed);
                break;

            case POSITIVE_NEGATIVE:
            case TEXT:
            case MULTI_OPTION:
            default:
                status = interpretQualitative(parameter, trimmed);
                interpretation = interpretationText(parameter, status, trimmed, null);
                break;
        }

        result.setStatus(status);
        result.setInterpretation(interpretation);
        return result;
    }

    private ParameterStatus interpretNumeric(TestParameter parameter, ReferenceRange range, String value) {
        double v;
        try {
            v = Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            return ParameterStatus.ABNORMAL;
        }

        if (range == null) {
            return ParameterStatus.ABNORMAL;
        }

        if (range.getCriticalLow() != null && v < range.getCriticalLow()) {
            return ParameterStatus.CRITICAL_LOW;
        }
        if (range.getCriticalHigh() != null && v > range.getCriticalHigh()) {
            return ParameterStatus.CRITICAL_HIGH;
        }
        if (range.getMinValue() != null && v < range.getMinValue()) {
            return ParameterStatus.LOW;
        }
        if (range.getMaxValue() != null && v > range.getMaxValue()) {
            return ParameterStatus.HIGH;
        }
        return ParameterStatus.NORMAL;
    }

    private ParameterStatus interpretQualitative(TestParameter parameter, String value) {
        List<InterpretationRule> rules = parameter.getInterpretationRules();
        if (rules != null) {
            for (InterpretationRule rule : rules) {
                if (Boolean.TRUE.equals(rule.getActive())
                        && rule.getValueMatch() != null
                        && rule.getValueMatch().equalsIgnoreCase(value)) {
                    return parseStatus(rule.getParameterStatus());
                }
            }
        }

        String v = value.toLowerCase(Locale.ROOT);
        if (v.contains("positive") || v.contains("reactive") || v.contains("detected")) {
            return ParameterStatus.POSITIVE;
        }
        if (v.contains("negative") || v.contains("non-reactive") || v.contains("not detected")) {
            return ParameterStatus.NEGATIVE;
        }
        if (v.contains("borderline") || v.contains("equivocal") || v.contains("indeterminate")) {
            return ParameterStatus.BORDERLINE;
        }
        return ParameterStatus.ABNORMAL;
    }

    private String interpretationText(TestParameter parameter, ParameterStatus status, String valueMatch, String rawValue) {
        List<InterpretationRule> rules = parameter.getInterpretationRules();
        if (rules != null) {
            for (InterpretationRule rule : rules) {
                if (!Boolean.TRUE.equals(rule.getActive())) continue;
                if (rule.getParameterStatus() == null) continue;
                if (!rule.getParameterStatus().equalsIgnoreCase(status.name())) continue;
                if (rule.getValueMatch() != null && !rule.getValueMatch().equalsIgnoreCase(status.name())
                        && valueMatch != null && !rule.getValueMatch().equalsIgnoreCase(valueMatch)) {
                    continue;
                }
                if (valueMatch == null && rule.getValueMatch() != null) continue;
                return rule.getInterpretationText();
            }
        }

        String name = parameter.getParameterName() != null ? parameter.getParameterName() : "Parameter";
        String rangeText = "N/A";
        String valueText = rawValue != null ? rawValue : "";
        String result = String.format("Result: %s. Reference range: %s.", valueText, rangeText);

        switch (status) {
            case CRITICAL_LOW:
                return name + " is critically below the normal range. Immediate medical attention required.";
            case CRITICAL_HIGH:
                return name + " is critically above the normal range. Immediate medical attention required.";
            case LOW:
                return name + " is below the normal range.";
            case HIGH:
                return name + " is above the normal range.";
            case POSITIVE:
                return name + " is positive.";
            case NEGATIVE:
                return name + " is negative.";
            case REACTIVE:
                return name + " is reactive.";
            case NON_REACTIVE:
                return name + " is non-reactive.";
            case BORDERLINE:
                return name + " is borderline. Repeat testing or further correlation advised.";
            case ABNORMAL:
                return name + " result is abnormal. Clinical correlation required.";
            case NORMAL:
            default:
                return name + " is within the normal range. " + result;
        }
    }

    public static ParameterStatus parseStatus(String s) {
        if (s == null) return ParameterStatus.ABNORMAL;
        try {
            return ParameterStatus.valueOf(s.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return ParameterStatus.ABNORMAL;
        }
    }
}
