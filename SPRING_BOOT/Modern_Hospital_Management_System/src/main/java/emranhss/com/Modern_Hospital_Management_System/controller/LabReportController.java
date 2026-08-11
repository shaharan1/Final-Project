package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.InterpretPreviewRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.LabReportCreateRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.VerifyLabReportRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.*;
import emranhss.com.Modern_Hospital_Management_System.service.lab.LabReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lab/reports")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class LabReportController {

    private final LabReportService labReportService;

    @PostMapping
    public ResponseEntity<LabReportResponse> createReport(@RequestBody LabReportCreateRequest request) {
        return ResponseEntity.ok(labReportService.createReport(request));
    }

    @GetMapping
    public ResponseEntity<List<LabReportResponse>> getAllReports() {
        return ResponseEntity.ok(labReportService.getAllReports());
    }

    @GetMapping("/dashboard")
    public ResponseEntity<LabDashboardResponse> getDashboard() {
        return ResponseEntity.ok(labReportService.getDashboard());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabReportResponse> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok(labReportService.getReportById(id));
    }

    @GetMapping("/test-order/{testOrderId}")
    public ResponseEntity<LabReportResponse> getReportByTestOrderId(@PathVariable Long testOrderId) {
        return ResponseEntity.ok(labReportService.getReportByTestOrderId(testOrderId));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<LabReportResponse>> getReportsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(labReportService.getReportsByPatient(patientId));
    }

    @PostMapping("/interpret")
    public ResponseEntity<InterpretPreviewResponse> preview(@RequestBody InterpretPreviewRequest request) {
        return ResponseEntity.ok(labReportService.preview(request));
    }

    @PutMapping("/{id}/verify")
    public ResponseEntity<LabReportResponse> verifyReport(@PathVariable Long id, @RequestBody VerifyLabReportRequest request) {
        return ResponseEntity.ok(labReportService.verifyReport(id, request));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadReportPdf(@PathVariable Long id) {
        byte[] pdf = labReportService.generateReportPdf(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Pathology_Report_" + id + ".pdf");
        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}
