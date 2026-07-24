package emranhss.com.Modern_Hospital_Management_System.controller;


import emranhss.com.Modern_Hospital_Management_System.dto.request.PrescriptionRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PrescriptionResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Prescription;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.PrescriptionRepository;
import emranhss.com.Modern_Hospital_Management_System.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PrescriptionController {


    private final PrescriptionService prescriptionService;
    private final PrescriptionRepository prescriptionRepository;

    @PostMapping
    public ResponseEntity<PrescriptionResponse> createPrescription(@RequestBody PrescriptionRequest request) {
        return new ResponseEntity<>(prescriptionService.createPrescription(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponse> getPrescriptionById(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PrescriptionResponse>> getPrescriptionsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByPatientId(patientId));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long id) throws Exception {

        byte[] pdf = prescriptionService.generatePdf(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=Prescription.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
