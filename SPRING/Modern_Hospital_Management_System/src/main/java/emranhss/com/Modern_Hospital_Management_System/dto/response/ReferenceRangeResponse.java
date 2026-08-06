package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.Data;

@Data
public class ReferenceRangeResponse {
    private Long id;
    private Long testParameterId;
    private String genderScope;
    private Integer minAgeYears;
    private Integer maxAgeYears;
    private Double minValue;
    private Double maxValue;
    private Double criticalLow;
    private Double criticalHigh;
    private String displayRange;
    private Integer priority;
    private Boolean active;
}
