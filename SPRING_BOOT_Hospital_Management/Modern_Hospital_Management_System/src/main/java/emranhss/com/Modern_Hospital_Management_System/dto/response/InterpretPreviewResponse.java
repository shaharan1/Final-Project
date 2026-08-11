package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.Data;

@Data
public class InterpretPreviewResponse {
    private Long parameterId;
    private String resultValue;
    private String unit;
    private String status;
    private String statusLabel;
    private String interpretation;
    private String referenceRangeDisplay;
    private Boolean abnormal;
    private Boolean critical;
}
