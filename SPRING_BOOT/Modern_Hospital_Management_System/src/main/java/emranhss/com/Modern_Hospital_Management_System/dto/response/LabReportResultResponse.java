package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.Data;

@Data
public class LabReportResultResponse {
    private Long id;
    private Long parameterId;
    private String parameterName;
    private String parameterCode;
    private String unit;
    private String resultValue;
    private String status;
    private String statusLabel;
    private String interpretation;
    private Boolean abnormal;
    private Boolean critical;
    private Integer displayOrder;
    private String referenceRangeDisplay;
}
