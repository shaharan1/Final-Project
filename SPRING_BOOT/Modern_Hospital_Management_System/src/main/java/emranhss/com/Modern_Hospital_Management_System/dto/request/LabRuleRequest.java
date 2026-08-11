package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.Data;

@Data
public class LabRuleRequest {
    private String ruleCode;
    private String ruleName;
    private String conditions;
    private String finalImpression;
    private String recommendation;
    private Integer priority;
    private Boolean active;
}
