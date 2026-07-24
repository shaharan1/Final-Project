package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.ReportMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.ReportRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.ReportResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Doctor;
import emranhss.com.Modern_Hospital_Management_System.entity.Patient;
import emranhss.com.Modern_Hospital_Management_System.entity.Report;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.DoctorRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.PatientRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.ReportRepository;
import emranhss.com.Modern_Hospital_Management_System.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ReportMapper reportMapper;

    @Override
    @Transactional
    public ReportResponse createReport(ReportRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + request.getPatientId()));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + request.getDoctorId()));

        Report report = reportMapper.toEntity(request);
        report.setPatient(patient);
        report.setDoctor(doctor);

        return reportMapper.toResponse(reportRepository.save(report));
    }

    @Override
    @Transactional(readOnly = true)
    public ReportResponse getReportById(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with ID: " + id));
        return reportMapper.toResponse(report);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportResponse> getAllReports() {
        return reportRepository.findAll().stream()
                .map(reportMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReportResponse updateReport(Long id, ReportRequest request) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with ID: " + id));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + request.getPatientId()));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + request.getDoctorId()));

        report.setReportResult(request.getReportResult());
        report.setDescription(request.getDescription());
        report.setSampleId(request.getSampleId());
        report.setInterpretation(request.getInterpretation());
        report.setPreparedBy(request.getPreparedBy());
        report.setTestDate(request.getTestDate());
        report.setDeliveryDate(request.getDeliveryDate());
        report.setPatient(patient);
        report.setDoctor(doctor);

        return reportMapper.toResponse(reportRepository.save(report));
    }

    @Override
    @Transactional
    public void deleteReport(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with ID: " + id));
        reportRepository.delete(report);
    }
}
