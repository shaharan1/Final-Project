package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.DoctorChargeMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.DoctorChargeRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.DoctorChargeResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.AdmittedPatient;
import emranhss.com.Modern_Hospital_Management_System.entity.BedBooking;
import emranhss.com.Modern_Hospital_Management_System.entity.Doctor;
import emranhss.com.Modern_Hospital_Management_System.entity.DoctorCharge;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.AdmittedPatientRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.BedBookingRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.DoctorChargeRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.DoctorRepository;
import emranhss.com.Modern_Hospital_Management_System.service.DoctorChargeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorChargeServiceImp implements DoctorChargeService {

    private final DoctorChargeRepository doctorChargeRepository;
    private final DoctorRepository doctorRepository;
    private final BedBookingRepository bedBookingRepository;
    private final AdmittedPatientRepository admittedPatientRepository;
    private final DoctorChargeMapper doctorChargeMapper;

    public DoctorChargeServiceImp(DoctorChargeRepository doctorChargeRepository,
                                  DoctorRepository doctorRepository,
                                  BedBookingRepository bedBookingRepository,
                                  AdmittedPatientRepository admittedPatientRepository,
                                  DoctorChargeMapper doctorChargeMapper) {
        this.doctorChargeRepository = doctorChargeRepository;
        this.doctorRepository = doctorRepository;
        this.bedBookingRepository = bedBookingRepository;
        this.admittedPatientRepository = admittedPatientRepository;
        this.doctorChargeMapper = doctorChargeMapper;
    }

    @Override
    public DoctorChargeResponse create(DoctorChargeRequest request) {
        Doctor doctor = resolveDoctor(request.getDoctorId());
        BedBooking bedBooking = resolveBedBooking(request.getBedBookingId());
        AdmittedPatient admittedPatient = resolveAdmittedPatient(request.getAdmittedPatientId());

        DoctorCharge charge = doctorChargeMapper.toEntity(request, doctor, bedBooking, admittedPatient);
        DoctorCharge saved = doctorChargeRepository.save(charge);
        return doctorChargeMapper.toResponse(saved);
    }

    @Override
    public DoctorChargeResponse getById(Long id) {
        DoctorCharge charge = findChargeOrThrow(id);
        return doctorChargeMapper.toResponse(charge);
    }

    @Override
    public List<DoctorChargeResponse> getAll() {
        return doctorChargeRepository.findAll()
                .stream()
                .map(doctorChargeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorChargeResponse update(Long id, DoctorChargeRequest request) {
        DoctorCharge charge = findChargeOrThrow(id);
        Doctor doctor = resolveDoctor(request.getDoctorId());
        BedBooking bedBooking = resolveBedBooking(request.getBedBookingId());
        AdmittedPatient admittedPatient = resolveAdmittedPatient(request.getAdmittedPatientId());

        doctorChargeMapper.updateEntity(charge, request, doctor, bedBooking, admittedPatient);
        DoctorCharge updated = doctorChargeRepository.save(charge);
        return doctorChargeMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        DoctorCharge charge = findChargeOrThrow(id);
        doctorChargeRepository.delete(charge);
    }

    private DoctorCharge findChargeOrThrow(Long id) {
        return doctorChargeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DoctorCharge not found with id: " + id));
    }

    private Doctor resolveDoctor(Long doctorId) {
        if (doctorId == null) {
            return null;
        }
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));
    }

    private BedBooking resolveBedBooking(Long bedBookingId) {
        if (bedBookingId == null) {
            return null;
        }
        return bedBookingRepository.findById(bedBookingId)
                .orElseThrow(() -> new ResourceNotFoundException("BedBooking not found with id: " + bedBookingId));
    }

    private AdmittedPatient resolveAdmittedPatient(Long admittedPatientId) {
        if (admittedPatientId == null) {
            return null;
        }
        return admittedPatientRepository.findById(admittedPatientId)
                .orElseThrow(() -> new ResourceNotFoundException("AdmittedPatient not found with id: " + admittedPatientId));
    }
}