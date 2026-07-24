package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.request.ReportRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.ReportResponse;
import emranhss.com.Modern_Hospital_Management_System.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportResponse> createReport(@RequestBody ReportRequest request) {
        return new ResponseEntity<>(reportService.createReport(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponse> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @GetMapping
    public ResponseEntity<List<ReportResponse>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportResponse> updateReport(@PathVariable Long id, @RequestBody ReportRequest request) {
        return ResponseEntity.ok(reportService.updateReport(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
