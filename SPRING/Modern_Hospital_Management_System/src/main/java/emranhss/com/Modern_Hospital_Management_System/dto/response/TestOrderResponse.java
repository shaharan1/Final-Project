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
    private Long patientId;
    private String patientName;
    private String patientCode;
    private Long doctorId;
    private String doctorName;
    private Long prescriptionId;
}
