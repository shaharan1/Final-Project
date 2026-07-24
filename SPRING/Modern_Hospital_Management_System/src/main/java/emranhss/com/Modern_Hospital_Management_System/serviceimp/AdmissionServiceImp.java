package emranhss.com.Modern_Hospital_Management_System.serviceimp;


import emranhss.com.Modern_Hospital_Management_System.dto.mapper.AdmissionMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.AdmissionRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.AdmissionResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.*;
import emranhss.com.Modern_Hospital_Management_System.enums.BedStatus;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.*;
import emranhss.com.Modern_Hospital_Management_System.service.AdmissionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdmissionServiceImp implements AdmissionService {


    private final AdmittedPatientRepository admittedPatientRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final BedRepository bedRepository;
    private final BedBookingRepository bedBookingRepository;
    private final AdmissionMapper mapper;
    private final AdmittedPatientRepository admissionRepository;

    @Override
    @Transactional
    public AdmissionResponse admitPatient(AdmissionRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient records missing"));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor records missing"));

        Bed bed = (Bed) bedRepository.findById(request.getBedId())
                .orElseThrow(() -> new ResourceNotFoundException("Target bed item missing"));

        if (!bed.getStatus().equals(BedStatus.AVAILABLE)) {
            throw new IllegalStateException("Selected bed is currently occupied or under maintenance!");
        }

        // 1. Create Inpatient Admission Entry
        AdmittedPatient admission = new AdmittedPatient();
        admission.setPatient(patient);
        admission.setPrimaryDoctor(doctor);
        admission.setInitialDiagnosis(request.getInitialDiagnosis());
        admission.setAdmissionStatus("ADMITTED");
        AdmittedPatient savedAdmission = admittedPatientRepository.save(admission);

        // 2. Lock Bed State
        bed.setStatus(BedStatus.OCCUPIED);
        bedRepository.save(bed);

        // 3. Document Transactional Bed Booking Matrix
        BedBooking booking = new BedBooking();
        booking.setAdmittedPatient(savedAdmission);
        booking.setBed(bed);
        booking.setStartTime(LocalDateTime.now());
        booking.setActive(true);
        BedBooking savedBooking = bedBookingRepository.save(booking);

        return mapper.toResponse(savedAdmission, savedBooking);
    }

    @Override
    @Transactional
    public AdmissionResponse dischargePatient(Long admissionId) {
        AdmittedPatient admission = admittedPatientRepository.findById(admissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Active admission entry missing"));

        admission.setAdmissionStatus("DISCHARGED");
        admission.setDischargeDate(LocalDateTime.now());
        AdmittedPatient updatedAdmission = admittedPatientRepository.save(admission);

        // Terminate and free up active bed mapping loops
        BedBooking activeBooking = bedBookingRepository.findByAdmittedPatientIdAndActiveTrue(admissionId)
                .orElse(null);

        if (activeBooking != null) {
            activeBooking.setActive(false);
            activeBooking.setEndTime(LocalDateTime.now());
            bedBookingRepository.save(activeBooking);

            Bed bed = activeBooking.getBed();
            bed.setStatus(BedStatus.AVAILABLE);
            bedRepository.save(bed);
        }

        return mapper.toResponse(updatedAdmission, activeBooking);
    }


//    =============NEW=============


    @Override
    public List<AdmissionResponse> getAllAdmissions() {

        return admissionRepository.findAll()
                .stream()
                .map(admission -> {

                    BedBooking booking = bedBookingRepository
                            .findByAdmittedPatientIdAndActiveTrue(admission.getId())
                            .orElse(null);

                    return mapper.toResponse(admission, booking);

                })
                .toList();

    }

    @Override
    public AdmissionResponse getAdmissionById(Long id) {

        AdmittedPatient admission = admissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admission Not Found"));

        BedBooking booking = bedBookingRepository
                .findByAdmittedPatientIdAndActiveTrue(id)
                .orElse(null);

        return mapper.toResponse(admission, booking);

    }

}
