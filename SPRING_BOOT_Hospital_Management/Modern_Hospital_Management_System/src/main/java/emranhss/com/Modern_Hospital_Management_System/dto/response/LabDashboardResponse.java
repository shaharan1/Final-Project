package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LabDashboardResponse {
    private long totalReports;
    private long normalReports;
    private long abnormalReports;
    private long criticalReports;
    private long denguePositive;
    private long pendingVerification;
    private long readyReports;
    private List<LabReportResponse> recentReports = new ArrayList<>();
    private List<String> criticalAlerts = new ArrayList<>();
}
