package emranhss.com.Modern_Hospital_Management_System.service.lab;

import emranhss.com.Modern_Hospital_Management_System.dto.request.VerifyLabReportRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.LabReportCreateRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.InterpretPreviewRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.*;

import java.util.List;

public interface LabReportService {

    LabReportResponse createReport(LabReportCreateRequest request);

    LabReportResponse getReportById(Long id);

    LabReportResponse getReportByTestOrderId(Long testOrderId);

    List<LabReportResponse> getReportsByPatient(Long patientId);

    List<LabReportResponse> getAllReports();

    InterpretPreviewResponse preview(InterpretPreviewRequest request);

    LabReportResponse verifyReport(Long id, VerifyLabReportRequest request);

    LabDashboardResponse getDashboard();

    byte[] generateReportPdf(Long id);
}
