package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.Data;

@Data
public class VerifyLabReportRequest {
    private String specialistName;
    private String specialistDesignation;
    private String specialistSignature;
    private String verificationNotes;
}
