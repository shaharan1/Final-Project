package emranhss.com.Modern_Hospital_Management_System.controller;

import emranhss.com.Modern_Hospital_Management_System.dto.response.TestOrderResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Tests;
import emranhss.com.Modern_Hospital_Management_System.pdf.LabReportPdfGenerator;
import emranhss.com.Modern_Hospital_Management_System.repository.TestsRepository;
import emranhss.com.Modern_Hospital_Management_System.service.TestsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/test-orders")
@RequiredArgsConstructor
public class TestsController {

    private final TestsService testsService;
    private final TestsRepository testsRepository;

    @GetMapping
    public ResponseEntity<List<TestOrderResponse>> getAllTestOrders() {
        return ResponseEntity.ok(testsService.getAllTestOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestOrderResponse> getTestOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(testsService.getTestOrderById(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TestOrderResponse>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(testsService.getTestOrdersByStatus(status));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<TestOrderResponse>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(testsService.getTestOrdersByPatient(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<TestOrderResponse>> getByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(testsService.getTestOrdersByDoctor(doctorId));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        return ResponseEntity.ok(testsService.getStats());
    }

    // Workflow transitions
    @PutMapping("/{id}/collect-sample")
    public ResponseEntity<TestOrderResponse> collectSample(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(testsService.collectSample(id,
                body.get("collectorName"), body.get("sampleType")));
    }

    @PutMapping("/{id}/receive-sample")
    public ResponseEntity<TestOrderResponse> receiveSample(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(testsService.receiveSample(id, body.get("receivedBy")));
    }

    @PutMapping("/{id}/start-testing")
    public ResponseEntity<TestOrderResponse> startTesting(@PathVariable Long id) {
        return ResponseEntity.ok(testsService.startTesting(id));
    }

    @PutMapping("/{id}/enter-result")
    public ResponseEntity<TestOrderResponse> enterResult(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(testsService.enterResult(id,
                body.get("resultValue"), body.get("resultNotes"), body.get("enteredBy")));
    }

    @PutMapping("/{id}/verify")
    public ResponseEntity<TestOrderResponse> verifyResult(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(testsService.verifyResult(id,
                body.get("verifiedBy"), body.get("verificationNotes")));
    }

    @GetMapping("/{id}/report/pdf")
    public ResponseEntity<byte[]> downloadReportPdf(@PathVariable Long id) {
        Tests test = testsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test not found"));
        try {
            ByteArrayOutputStream pdfBytes = LabReportPdfGenerator.generate(test);
            test.setReportFilePath("lab_report_" + id + ".pdf");
            testsRepository.save(test);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment",
                    "Lab_Report_" + test.getPatient().getPatientCode() + ".pdf");
            return ResponseEntity.ok().headers(headers).body(pdfBytes.toByteArray());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
