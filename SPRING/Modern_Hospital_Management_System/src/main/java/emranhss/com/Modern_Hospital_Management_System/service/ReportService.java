package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.ReportRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.ReportResponse;
import java.util.List;

public interface ReportService {
    ReportResponse createReport(ReportRequest request);
    ReportResponse getReportById(Long id);
    List<ReportResponse> getAllReports();
    ReportResponse updateReport(Long id, ReportRequest request);
    void deleteReport(Long id);
}
