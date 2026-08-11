package emranhss.com.Modern_Hospital_Management_System.service.lab;

import emranhss.com.Modern_Hospital_Management_System.enums.ReportStatus;
import lombok.Data;

@Data
public class ReportAnalysis {
    private ReportStatus reportStatus;
    private String finalImpression;
    private String recommendation;
    private long normalCount;
    private long abnormalCount;
    private long criticalCount;
}
