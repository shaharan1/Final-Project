package emranhss.com.Modern_Hospital_Management_System.controller;


import emranhss.com.Modern_Hospital_Management_System.dto.mapper.PrescriptionMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.PrescriptionRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PrescriptionResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.*;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.*;
import emranhss.com.Modern_Hospital_Management_System.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PrescriptionController {


    private final PrescriptionService prescriptionService;
    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionMapper prescriptionMapper;
    private final MedicineRepository medicineRepository;
    private final TestMasterRepository testMasterRepository;
    private final TestsRepository testsRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @PostMapping
    public ResponseEntity<PrescriptionResponse> createPrescription(@RequestBody PrescriptionRequest request) {
        return new ResponseEntity<>(prescriptionService.createPrescription(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PrescriptionResponse>> getAllPrescriptions() {
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        List<PrescriptionResponse> responses = prescriptions.stream()
                .map(prescriptionMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponse> getPrescriptionById(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<PrescriptionResponse>> getPrescriptionsByDoctorId(@PathVariable Long doctorId) {
        List<Prescription> prescriptions = prescriptionRepository.findByDoctorId(doctorId);
        List<PrescriptionResponse> responses = prescriptions.stream()
                .map(prescriptionMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PrescriptionResponse>> getPrescriptionsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByPatientId(patientId));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<PrescriptionResponse>> getPendingPrescriptions() {
        List<PrescriptionResponse> responses = prescriptionRepository.findPending().stream()
                .map(prescriptionMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}/dispensed")
    public ResponseEntity<PrescriptionResponse> markDispensed(@PathVariable Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
        prescription.setDispensed(true);
        return ResponseEntity.ok(prescriptionMapper.toResponse(prescriptionRepository.save(prescription)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionResponse> updatePrescription(@PathVariable Long id, @RequestBody PrescriptionRequest request) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
        
        prescription.setDiagnosis(request.getDiagnosis());
        prescription.setChiefComplaints(request.getChiefComplaints());
        prescription.setSymptoms(request.getSymptoms());
        prescription.setBloodPressure(request.getBloodPressure());
        prescription.setPulseRate(request.getPulseRate());
        prescription.setBodyTemperature(request.getBodyTemperature());
        prescription.setWeight(request.getWeight());
        prescription.setNotes(request.getNotes());
        prescription.setNextFollowUpDate(request.getNextFollowUpDate());

        // Update prescription items
        if (request.getPrescriptionItems() != null) {
            prescription.getPrescriptionItems().clear();
            for (var itemDto : request.getPrescriptionItems()) {
                Medicine medicine = medicineRepository.findById(itemDto.getMedicineId())
                        .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + itemDto.getMedicineId()));
                PrescriptionItem item = new PrescriptionItem();
                item.setPrescription(prescription);
                item.setMedicine(medicine);
                item.setDosage(itemDto.getDosage());
                item.setDuration(itemDto.getDuration());
                item.setInstruction(itemDto.getInstruction());
                prescription.getPrescriptionItems().add(item);
            }
        }

        Prescription saved = prescriptionRepository.save(prescription);

        // Update tests: delete old, add new
        if (request.getTestIds() != null) {
            List<Tests> existingTests = testsRepository.findByPrescriptionId(id);
            testsRepository.deleteAll(existingTests);

            if (!request.getTestIds().isEmpty()) {
                List<Tests> newTests = new ArrayList<>();
                for (Long testId : request.getTestIds()) {
                    TestMaster master = testMasterRepository.findById(testId)
                            .orElseThrow(() -> new ResourceNotFoundException("Test not found with id: " + testId));
                    Tests test = new Tests();
                    test.setPrescription(saved);
                    test.setPatient(saved.getPatient());
                    test.setPrescribedBy(saved.getDoctor());
                    test.setTestMaster(master);
                    test.setOrderStatus("PENDING");
                    newTests.add(test);
                }
                testsRepository.saveAll(newTests);
            }
        }

        return ResponseEntity.ok(prescriptionMapper.toResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
        prescriptionRepository.delete(prescription);
        return ResponseEntity.noContent().build();
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
