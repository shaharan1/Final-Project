package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
    private Long id;
    private String reportResult;
    private String description;
    private String sampleId;
    private String interpretation;
    private String preparedBy;
    private Date testDate;
    private Date createDate;
    private Date deliveryDate;

    // Flattened Patient details for easy JSON UI rendering
    private Long patientId;
    private String patientCode;
    private String patientFullName;
    private String patientPhone;
    private String patientGender;
    private String patientBloodGroup;

    // Flattened Doctor details
    private Long doctorId;
    private String doctorName;
    private String doctorSpecialization;
}
