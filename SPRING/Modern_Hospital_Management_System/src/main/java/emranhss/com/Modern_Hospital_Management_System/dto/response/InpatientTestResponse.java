package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InpatientTestResponse {


    private Long testAdmissionId;
    private Long testOrderId;
    private String testCode;
    private String testName;
    private double billedAmount;
    private String orderStatus;
    private String billingStatus;
    private String patientName;
    private String doctorName;
    private LocalDateTime orderedDate;
}
