package emranhss.com.Modern_Hospital_Management_System.serviceimp;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import emranhss.com.Modern_Hospital_Management_System.dto.mapper.PrescriptionMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.PrescriptionRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PrescriptionResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.*;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.pdf.*;
import emranhss.com.Modern_Hospital_Management_System.repository.*;
import emranhss.com.Modern_Hospital_Management_System.service.PrescriptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.itextpdf.text.pdf.PdfWriter;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.time.format.DateTimeFormatter;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImp implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final MedicineRepository medicineRepository;
    private final PrescriptionMapper prescriptionMapper;
    private final TestsRepository testsRepository;
    private final TestMasterRepository testMasterRepository;

    @Override
    @Transactional
    public PrescriptionResponse createPrescription(PrescriptionRequest request) {


        System.out.println("AppointmentId = " + request.getAppointmentId());
        System.out.println("DoctorId = " + request.getDoctorId());
        System.out.println("PatientId = " + request.getPatientId());

        request.getPrescriptionItems().forEach(item -> {
            System.out.println("MedicineId = " + item.getMedicineId());
        });

        System.out.println("TestIds = " + request.getTestIds());

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + request.getAppointmentId()));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));

        Prescription prescription = new Prescription();
        prescription.setAppointment(appointment);
        prescription.setDoctor(doctor);
        prescription.setPatient(patient);
        prescription.setDiagnosis(request.getDiagnosis());
        prescription.setChiefComplaints(request.getChiefComplaints());
        prescription.setSymptoms(request.getSymptoms());
        prescription.setBloodPressure(request.getBloodPressure());
        prescription.setPulseRate(request.getPulseRate());
        prescription.setBodyTemperature(request.getBodyTemperature());
        prescription.setWeight(request.getWeight());
        prescription.setNotes(request.getNotes());
        prescription.setNextFollowUpDate(request.getNextFollowUpDate());

        // Process and map prescription items (medicines)
        if (request.getPrescriptionItems() != null) {
            List<PrescriptionItem> items = request.getPrescriptionItems().stream()
                    .map(itemDto -> {
                        Medicine medicine = medicineRepository.findById(itemDto.getMedicineId())
                                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + itemDto.getMedicineId()));

                        PrescriptionItem item = new PrescriptionItem();
                        item.setPrescription(prescription);
                        item.setMedicine(medicine);
                        item.setDosage(itemDto.getDosage());
                        item.setDuration(itemDto.getDuration());
                        item.setInstruction(itemDto.getInstruction());
                        return item;

                    })
                    .collect(Collectors.toList());

            prescription.setPrescriptionItems(items);
        }

        // Save prescription entity first
        Prescription savedPrescription = prescriptionRepository.save(prescription);

        // Process and assign laboratory tests
        if (request.getTestIds() != null && !request.getTestIds().isEmpty()) {
            List<Tests> assignedTests = request.getTestIds().stream()
                    .map(testId -> {
                        TestMaster master = testMasterRepository.findById(testId)
                                .orElseThrow(() -> new ResourceNotFoundException("Test not found with id: " + testId));

                        Tests test = new Tests();
                        test.setPrescription(savedPrescription);
                        test.setPatient(patient);
                        test.setPrescribedBy(doctor);
                        test.setTestMaster(master);
                        test.setOrderStatus("PENDING");
                        return test;
                    })
                    .collect(Collectors.toList());

            testsRepository.saveAll(assignedTests); // Batch save optimized performance
        }

        return prescriptionMapper.toResponse(savedPrescription);
    }

    @Override
    public PrescriptionResponse getPrescriptionById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
        return prescriptionMapper.toResponse(prescription);
    }

    @Override
    public List<PrescriptionResponse> getPrescriptionsByPatientId(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId).stream()
                .map(prescriptionMapper::toResponse)
                .collect(Collectors.toList());
    }


    @Override
    public byte[] generatePdf(Long id) {

        // =====================================================
        // Get Prescription
        // =====================================================

        Prescription prescription =
                prescriptionRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Prescription not found"));

        try {

            // =====================================================
            // PDF Initialization
            // =====================================================

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document document = new Document(
                    PageSize.A4,
                    25,
                    25,
                    25,
                    25
            );

            PdfWriter.getInstance(document, out);

            document.open();

            // =====================================================
            // Load Tests
            // =====================================================

            List<Tests> tests =
                    testsRepository.findByPrescriptionId(
                            prescription.getId()
                    );

            // =====================================================
            // Header
            // =====================================================

            PdfHeader.add(document);

            // =====================================================
            // Patient & Doctor Section
            // =====================================================



            PdfPatientDoctorSection.add(
                    
                    document, prescription

            );

            // =====================================================
            // Vital Section
            // =====================================================

            PdfVitalSection.add(
                    document,
                    prescription
            );

            // =====================================================
            // Diagnosis Section
            // =====================================================

            PdfDiagnosisSection.add(
                    document,
                    prescription
            );

            // =====================================================
            // Medicine Section
            // =====================================================

            PdfMedicineTable.add(
                    document,
                    prescription
            );

            // =====================================================
            // Test Section
            // =====================================================

            PdfTestTable.add(
                    document,
                    tests
            );

            // =====================================================
            // Advice Section
            // =====================================================

            PdfAdviceSection.add(
                    document,
                    prescription
            );

            // =====================================================
            // Footer Section
            // =====================================================

            PdfFooter.add(
                    document,
                    prescription
            );

            // =====================================================
            // Close Document
            // =====================================================

            document.close();

            return out.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException("PDF Generation Failed", e);

        }

    }

}




