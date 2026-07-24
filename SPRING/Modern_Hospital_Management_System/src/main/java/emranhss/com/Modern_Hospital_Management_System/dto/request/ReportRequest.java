package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {
    private String reportResult;
    private String description;
    private String sampleId;
    private String interpretation;
    private String preparedBy;
    private Date testDate;
    private Date deliveryDate;
    private Long patientId; // Pass Patient ID from frontend
    private Long doctorId;  // Pass Doctor ID from frontend
}
