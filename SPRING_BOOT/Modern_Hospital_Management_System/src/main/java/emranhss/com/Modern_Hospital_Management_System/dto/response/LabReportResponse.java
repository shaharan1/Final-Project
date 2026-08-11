package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class LabReportResponse {
    private Long id;
    private String reportNumber;
    private Long testOrderId;
    private Long testMasterId;
    private String testCode;
    private String testName;
    private String orderStatus;

    private Long patientId;
    private String patientCode;
    private String patientName;
    private String patientGender;
    private String patientAge;
    private String patientPhone;

    private Long doctorId;
    private String doctorName;
    private String doctorSpecialization;

    private String reportStatus;
    private String statusLabel;
    private String finalImpression;
    private String recommendation;

    private String specialistName;
    private String specialistDesignation;
    private String specialistSignature;

    private String sampleType;
    private LocalDateTime sampleCollectedDate;
    private LocalDateTime sampleReceivedDate;

    private String createdBy;
    private LocalDateTime createdDate;
    private LocalDateTime reportedDate;

    private List<LabReportResultResponse> results = new ArrayList<>();
}
