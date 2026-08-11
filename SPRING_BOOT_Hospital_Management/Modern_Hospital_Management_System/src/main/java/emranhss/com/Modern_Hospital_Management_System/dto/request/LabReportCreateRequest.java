package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LabReportCreateRequest {
    private Long testOrderId;
    private String enteredBy;
    private List<LabReportResultEntry> results = new ArrayList<>();
}
