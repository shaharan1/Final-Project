package emranhss.com.Modern_Hospital_Management_System.dto.response;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdmissionResponse {


    private Long admissionId;
    private Long patientId;
    private String patientName;
    private String patientCode;
    private String doctorName;
    private String assignedBedNumber;
    private String wardName;
    private String initialDiagnosis;
    private LocalDateTime admissionDate;
    private String status;
}
