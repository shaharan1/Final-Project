package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TestParameterResponse {
    private Long id;
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
    private List<ReferenceRangeResponse> referenceRanges = new ArrayList<>();
    private List<InterpretationRuleResponse> interpretationRules = new ArrayList<>();
}
