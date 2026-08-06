package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.Data;

@Data
public class InterpretPreviewRequest {
    private Long parameterId;
    private String resultValue;
    private String patientGender;
    private Integer ageYears;
}
