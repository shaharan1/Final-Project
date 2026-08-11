package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.DoctorDiscountMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.DoctorDiscountRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.DoctorDiscountResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Doctor;
import emranhss.com.Modern_Hospital_Management_System.entity.DoctorDiscount;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.DoctorDiscountRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.DoctorRepository;
import emranhss.com.Modern_Hospital_Management_System.service.DoctorDiscountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorDiscountServiceImp implements DoctorDiscountService {

    private final DoctorDiscountRepository doctorDiscountRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorDiscountMapper doctorDiscountMapper;

    public DoctorDiscountServiceImp(DoctorDiscountRepository doctorDiscountRepository,
                                    DoctorRepository doctorRepository,
                                    DoctorDiscountMapper doctorDiscountMapper) {
        this.doctorDiscountRepository = doctorDiscountRepository;
        this.doctorRepository = doctorRepository;
        this.doctorDiscountMapper = doctorDiscountMapper;
    }

    @Override
    public DoctorDiscountResponse create(DoctorDiscountRequest request) {
        Doctor doctor = resolveDoctor(request.getDoctorId());
        DoctorDiscount discount = doctorDiscountMapper.toEntity(request, doctor);
        return doctorDiscountMapper.toResponse(doctorDiscountRepository.save(discount));
    }

    @Override
    public DoctorDiscountResponse getById(Long id) {
        return doctorDiscountMapper.toResponse(findOrThrow(id));
    }

    @Override
    public DoctorDiscountResponse getByDoctorId(Long doctorId) {
        return doctorDiscountMapper.toResponse(doctorDiscountRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "DoctorDiscount not found for doctor id: " + doctorId)));
    }

    @Override
    public List<DoctorDiscountResponse> getAll() {
        return doctorDiscountRepository.findAll().stream()
                .map(doctorDiscountMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorDiscountResponse> getActive() {
        return doctorDiscountRepository.findByActiveTrue().stream()
                .map(doctorDiscountMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorDiscountResponse update(Long id, DoctorDiscountRequest request) {
        DoctorDiscount discount = findOrThrow(id);
        Doctor doctor = null;
        if (request.getDoctorId() != null) {
            doctor = resolveDoctor(request.getDoctorId());
        }
        doctorDiscountMapper.updateEntity(discount, request, doctor);
        return doctorDiscountMapper.toResponse(doctorDiscountRepository.save(discount));
    }

    @Override
    public void delete(Long id) {
        DoctorDiscount discount = findOrThrow(id);
        doctorDiscountRepository.delete(discount);
    }

    private DoctorDiscount findOrThrow(Long id) {
        return doctorDiscountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DoctorDiscount not found with id: " + id));
    }

    private Doctor resolveDoctor(Long doctorId) {
        if (doctorId == null) {
            throw new ResourceNotFoundException("Doctor is required");
        }
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));
    }
}
