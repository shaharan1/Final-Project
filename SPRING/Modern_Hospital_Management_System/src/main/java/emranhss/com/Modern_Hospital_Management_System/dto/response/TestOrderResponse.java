package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestOrderResponse {
    private Long id;
    private String testCode;
    private String testName;
    private double standardPrice;
    private String normalRange;
    private String orderStatus;
    private LocalDateTime orderedDate;

    // Patient
    private Long patientId;
    private String patientName;
    private String patientCode;
    private String patientPhone;
    private String patientGender;

    // Doctor
    private Long doctorId;
    private String doctorName;
    private String doctorSpecialization;

    // Prescription
    private Long prescriptionId;

    // Sample Collection
    private String sampleCollectorName;
    private String sampleType;
    private LocalDateTime sampleCollectedDate;

    // Sample Received
    private LocalDateTime sampleReceivedDate;
    private String sampleReceivedBy;

    // Testing
    private LocalDateTime testingStartDate;

    // Result
    private String resultValue;
    private String resultNotes;
    private LocalDateTime resultEnteredDate;
    private String resultEnteredBy;

    // Verification
    private String verifiedBy;
    private LocalDateTime verifiedDate;
    private String verificationNotes;

    // Meta
    private LocalDateTime lastUpdated;
}
