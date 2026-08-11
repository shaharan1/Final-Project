package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TestParameterRequest {
    private Long testMasterId;
    private String parameterName;
    private String parameterCode;
    private String unit;
    private String resultType;
    private String allowedValues;
    private Integer displayOrder;
    private Integer decimalPrecision;
    private String normalText;
    private Boolean active;
    private List<ReferenceRangeRequest> referenceRanges = new ArrayList<>();
    private List<InterpretationRuleRequest> interpretationRules = new ArrayList<>();
}
