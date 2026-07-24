package emranhss.com.Modern_Hospital_Management_System.serviceimp;


import emranhss.com.Modern_Hospital_Management_System.dto.mapper.PatientMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.PatientRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.PatientResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Appointment;
import emranhss.com.Modern_Hospital_Management_System.entity.Patient;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.AppointmentRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.PatientRepository;
import emranhss.com.Modern_Hospital_Management_System.service.PatientService;
import emranhss.com.Modern_Hospital_Management_System.util.PatientCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImp implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final AppointmentRepository appointmentRepository;


    @Override
    public PatientResponse createPatient(PatientRequest request) {

        if (request.getEmail() != null && request.getEmail().trim().isEmpty()) {
            request.setEmail(null);
        }




        Patient patient = patientMapper.toEntity(request);

        Appointment appointment= new Appointment();


        // Appointment থেকে Auto Fill
        if (request.getAppointmentId() != null) {

             appointment = appointmentRepository
                    .findById(request.getAppointmentId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Appointment not found"));


            patient.setName(appointment.getPatientName());
            patient.setPhone(appointment.getMobileNumber());

        }

        patient.setPatientCode(generatePatientCode());

        Patient savedPatient = patientRepository.save(patient);

        appointment.setPatient(savedPatient);
        appointmentRepository.save(appointment);

        return patientMapper.toResponse(savedPatient);
    }

    @Override
    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        return patientMapper.toResponse(patient);
    }

    @Override
    public PatientResponse getPatientByCode(String code) {
        Patient patient = patientRepository.findByPatientCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with code: " + code));
        return patientMapper.toResponse(patient);
    }

    @Override
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patientMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PatientResponse updatePatient(Long id, PatientRequest request) {


        if (request.getEmail() != null && request.getEmail().trim().isEmpty()) {
            request.setEmail(null);
        }


        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        patientMapper.updateEntityFromRequest(request, patient);
        return patientMapper.toResponse(patientRepository.save(patient));
    }

    @Override
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        patientRepository.delete(patient);
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
