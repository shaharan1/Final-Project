package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.request.AppointmentRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.AppointmentResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.mapper.AppointmentMapper; // Inject Mapper
import emranhss.com.Modern_Hospital_Management_System.entity.*;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.*;
import emranhss.com.Modern_Hospital_Management_System.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ScheduleSlotRepository slotRepository;
    private final AppointmentMapper appointmentMapper; // Inject your fixed mapper component here

    @Override
    @Transactional
    public AppointmentResponse bookAppointment(AppointmentRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + request.getDoctorId()));

        ScheduleSlot slot = slotRepository.findByDoctorIdAndDateAndStartTime(
                        request.getDoctorId(),
                        request.getAppointmentDate(),
                        request.getAppointmentTime()
                )
                .orElseThrow(() -> new ResourceNotFoundException("No schedule slot found."));

        if (slot.getIsBooked()) {
            throw new IllegalStateException("This specific time slot has already been reserved!");
        }

        slot.setIsBooked(true);
        slotRepository.save(slot);

        Appointment appointment = new Appointment();

        if (request.getPatientId() != null) {
            Patient patient = patientRepository.findById(request.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found."));
            appointment.setPatient(patient);
            appointment.setPatientName(patient.getName());
            appointment.setMobileNumber(patient.getPhone());
        } else {
            appointment.setPatientName(request.getPatientName());
            appointment.setMobileNumber(request.getMobileNumber());
        }

        appointment.setAppointmentNumber("APT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        appointment.setSpecialization(request.getSpecialization());

//        -------------------------------------------
//        appointment.setName(request.getName());
//        appointment.setPhone(request.getPhone());
        appointment.setProblemDescription(request.getProblemDescription());
        appointment.setDoctor(doctor);
        appointment.setScheduleSlot(slot);
        appointment.setAppointmentDate(slot.getDate());
        appointment.setAppointmentTime(slot.getStartTime());
        appointment.setPaymentMethod(request.getPaymentMethod());
        appointment.setTransactionId(request.getTransactionId());

        //  Calculate and set the fee before saving
        Double totalFee = calculateFeeForPhone(request.getMobileNumber(), request.getDoctorId());
        appointment.setFeeCharged(totalFee);

        if ("Cash".equalsIgnoreCase(request.getPaymentMethod())) {
            appointment.setStatus("CONFIRMED");
        } else {
            appointment.setStatus("PENDING_VERIFICATION");
        }

        appointment.setCreatedDate(LocalDateTime.now());

        // Use clean mapper injection component mapping pattern
        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional
    public AppointmentResponse cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        appointment.setStatus("CANCELLED");

        if (appointment.getScheduleSlot() != null) {
            ScheduleSlot slot = appointment.getScheduleSlot();
            slot.setIsBooked(false);
            slotRepository.save(slot);
        }

        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> viewSchedule(LocalDate date) {
        return appointmentRepository.findByAppointmentDate(date).stream()
                .map(appointmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getDoctorWiseAppointments(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with ID: " + doctorId);
        }
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(appointmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculateFeeForPhone(String phone, Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));

        boolean hasSeenDoctorBefore = appointmentRepository.existsByMobileNumberAndDoctorIdAndStatus(phone, doctorId, "CONFIRMED");

        if (hasSeenDoctorBefore) {
            return doctor.getFollowUpFee();
        }
        return doctor.getConsultationFee();
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean isReturningPatient(String phone) {
        return appointmentRepository.existsByMobileNumberAndStatus(phone, "CONFIRMED");
    }


    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAllAppointments() {

        return appointmentRepository.findAll()
                .stream()
                .map(appointmentMapper::toResponse)
                .collect(Collectors.toList());

    }

    @Override
    public AppointmentResponse getAppointmentById(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Appointment not found with id: " + id));

        return appointmentMapper.toResponse(appointment);

    }

    @Override
    public AppointmentResponse getByAppointmentNumber(String appointmentNumber) {
        Appointment appointment = appointmentRepository
                .findByAppointmentNumber(appointmentNumber)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        return appointmentMapper.toResponse(appointment);    }

    //    --------------Filter Appointments---------------
@Override
@Transactional(readOnly = true)
public List<AppointmentResponse> filterAppointments(Long doctorId, LocalDate date) {

    List<Appointment> appointments;

    if (doctorId != null && date != null) {

        appointments = appointmentRepository
                .findByDoctorIdAndAppointmentDate(doctorId, date);

    } else if (doctorId != null) {

        appointments = appointmentRepository
                .findByDoctorId(doctorId);

    } else if (date != null) {

        appointments = appointmentRepository
                .findByAppointmentDate(date);

    } else {

        appointments = appointmentRepository.findAll();

    }

    return appointments.stream()
            .map(appointmentMapper::toResponse)
            .collect(Collectors.toList());

}
}
