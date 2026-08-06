package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.Data;

@Data
public class LabRuleResponse {
    private Long id;
    private String ruleCode;
    private String ruleName;
    private String conditions;
    private String finalImpression;
    private String recommendation;
    private Integer priority;
    private Boolean active;
}
