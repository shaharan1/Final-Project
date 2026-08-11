package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.Data;

@Data
public class InterpretationRuleRequest {
    private Long id;
    private Long testParameterId;
    private String parameterStatus;
    private String valueMatch;
    private String interpretationText;
    private Integer displayOrder;
    private Boolean active;
}
