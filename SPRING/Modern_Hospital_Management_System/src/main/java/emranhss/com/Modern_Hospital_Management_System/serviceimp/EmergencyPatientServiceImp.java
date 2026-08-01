package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.EmergencyPatientMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.mapper.EmergencyTimelineMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyPatientRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.request.EmergencyStatusUpdateRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyDashboardResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.EmergencyPatientResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyPatient;
import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyTimeline;
import emranhss.com.Modern_Hospital_Management_System.entity.Patient;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.*;
import emranhss.com.Modern_Hospital_Management_System.service.EmergencyPatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmergencyPatientServiceImp implements EmergencyPatientService {

    private final EmergencyPatientRepository emergencyPatientRepository;
    private final EmergencyPatientMapper emergencyPatientMapper;
    private final EmergencyTimelineMapper emergencyTimelineMapper;
    private final EmergencyTimelineRepository emergencyTimelineRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final EmergencyDoctorAssignmentRepository emergencyDoctorAssignmentRepository;
    private final EmergencyBedRepository emergencyBedRepository;
    private final EmergencyBillingRepository emergencyBillingRepository;
    private final PatientRepository patientRepository;
    private final NurseRepository nurseRepository;

    @Override
    @Transactional
    public EmergencyPatientResponse create(EmergencyPatientRequest request) {
        EmergencyPatient patient = emergencyPatientMapper.toEntity(request);
        patient.setEmergencyNumber(generateEmergencyNumber());
        patient.setSeverityLevel(request.getSeverityLevel() != null ? request.getSeverityLevel() : "MODERATE");
        patient.setStatus(request.getStatus() != null ? request.getStatus() : "WAITING");
        patient.setTriageLevel(request.getTriageLevel() != null ? request.getTriageLevel() : 3);

        Patient savedPatient;
        if (request.getPatientId() != null) {
            savedPatient = patientRepository.findById(request.getPatientId())
                    .orElseGet(() -> createNewPatient(request));
        } else {
            savedPatient = createNewPatient(request);
        }

       patient.setPatient(savedPatient);

        EmergencyPatient saved = emergencyPatientRepository.save(patient);

        EmergencyTimeline timeline = emergencyTimelineMapper.toEntity(
                saved.getId(), "ARRIVAL",
                "Patient arrived at emergency department", "System", "Emergency");
        emergencyTimelineRepository.save(timeline);

        return emergencyPatientMapper.toResponse(saved);
    }

    private Patient createNewPatient(EmergencyPatientRequest request) {
        Patient p = new Patient();
        p.setName(request.getPatientName());
        p.setPhone(request.getPhone());
        p.setAddress(request.getAddress());
        p.setNationalId(request.getNationalId());
        p.setBloodGroup(request.getBloodGroup());
        p.setEmergencyContactName(request.getEmergencyContactName());
        p.setEmergencyContactNumber(request.getEmergencyContactPhone());
        p.setRelationship(request.getEmergencyContactRelation());
        p.setPatientCode(generatePatientCode());
        return patientRepository.save(p);
    }

    @Override
    public EmergencyPatientResponse getById(Long id) {
        EmergencyPatient patient = emergencyPatientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency patient not found with id: " + id));
        return emergencyPatientMapper.toResponse(patient);
    }

    @Override
    public List<EmergencyPatientResponse> getAll() {
        return emergencyPatientRepository.findAll().stream()
                .map(emergencyPatientMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmergencyPatientResponse> search(String keyword) {
        return emergencyPatientRepository.searchByKeyword(keyword).stream()
                .map(emergencyPatientMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmergencyPatientResponse update(Long id, EmergencyPatientRequest request) {
        EmergencyPatient patient = emergencyPatientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency patient not found with id: " + id));
        emergencyPatientMapper.updateEntityFromRequest(request, patient);
        return emergencyPatientMapper.toResponse(emergencyPatientRepository.save(patient));
    }

    @Override
    @Transactional
    public EmergencyPatientResponse updateStatus(Long id, EmergencyStatusUpdateRequest request) {
        EmergencyPatient patient = emergencyPatientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency patient not found with id: " + id));
        patient.setStatus(request.getStatus());
        if ("DISCHARGED".equals(request.getStatus())) {
            patient.setDischargeTime(LocalDateTime.now());
        }
        return emergencyPatientMapper.toResponse(emergencyPatientRepository.save(patient));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        EmergencyPatient patient = emergencyPatientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency patient not found with id: " + id));
        emergencyPatientRepository.delete(patient);
    }

    @Override
    public EmergencyDashboardResponse getDashboard() {
        EmergencyDashboardResponse dashboard = new EmergencyDashboardResponse();

        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().atTime(LocalTime.MAX);

        dashboard.setEmergencyPatientsToday(emergencyPatientRepository.countByArrivalTimeBetween(todayStart, todayEnd));
        dashboard.setCriticalPatients(emergencyPatientRepository.countByStatus("CRITICAL"));
        dashboard.setWaitingPatients(emergencyPatientRepository.countByStatus("WAITING"));
        dashboard.setPatientsUnderTreatment(emergencyPatientRepository.countByStatus("UNDER_TREATMENT"));
        dashboard.setAdmittedFromEmergency(emergencyPatientRepository.countByStatus("ADMITTED"));
        dashboard.setAmbulancesActive(ambulanceRepository.countByStatus("ON_DUTY"));
        dashboard.setDoctorsOnDuty(emergencyDoctorAssignmentRepository.countByIsActive(true));
        dashboard.setAvailableEmergencyBeds(emergencyBedRepository.countByStatus("AVAILABLE"));
        dashboard.setIcuBedsAvailable(emergencyBedRepository.countByStatus("AVAILABLE"));
        dashboard.setNursesOnDuty((long) nurseRepository.findByOnDutyTrueAndActiveTrue().size());

        Double todayRevenue = emergencyBillingRepository.findAll().stream()
                .filter(b -> b.getPaidAt() != null && b.getPaidAt().isAfter(todayStart) && b.getPaidAt().isBefore(todayEnd))
                .mapToDouble(b -> b.getGrandTotal() != null ? b.getGrandTotal() : 0.0)
                .sum();
        dashboard.setTodaysEmergencyRevenue(todayRevenue);
        dashboard.setAverageWaitingTime(0.0);

        return dashboard;
    }

    @Override
    public List<EmergencyPatientResponse> getByStatus(String status) {
        return emergencyPatientRepository.findByStatus(status).stream()
                .map(emergencyPatientMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmergencyPatientResponse> getByTriageLevel(Integer triageLevel) {
        return emergencyPatientRepository.findByTriageLevel(triageLevel).stream()
                .map(emergencyPatientMapper::toResponse)
                .collect(Collectors.toList());
    }

    private String generateEmergencyNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().atTime(LocalTime.MAX);
        long todayCount = emergencyPatientRepository.countByArrivalTimeBetween(todayStart, todayEnd);
        int seq = (int) todayCount + 1;
        return "EMG-" + datePart + "-" + String.format("%04d", seq);
    }

    private String generatePatientCode() {

        Patient lastPatient = patientRepository.findTopByOrderByIdDesc();

        if (lastPatient == null || lastPatient.getPatientCode() == null) {
            return "PAT000001";
        }

        String lastCode = lastPatient.getPatientCode(); // PAT000125
        int number = Integer.parseInt(lastCode.substring(3));
        number++;

        return String.format("PAT%06d", number);
    }
}
