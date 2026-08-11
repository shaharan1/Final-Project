package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.DoctorMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.DoctorRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.DoctorResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Doctor;
import emranhss.com.Modern_Hospital_Management_System.entity.DoctorDepartment;
import emranhss.com.Modern_Hospital_Management_System.entity.User;
import emranhss.com.Modern_Hospital_Management_System.enums.Role;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.DoctorDepartmentRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.DoctorRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.UserRepository;
import emranhss.com.Modern_Hospital_Management_System.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImp implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorDepartmentRepository doctorDepartmentRepository;
    private final DoctorMapper doctorMapper;
    private final UserRepository userRepository;

    private  final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public DoctorResponse createDoctor(DoctorRequest request) {


        User user = new User();

        user.setEmail(request.getEmail());

        System.out.println(request.getEmail()+"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));// set password as hash code
        user.setRole(Role.Doctor);
        user.setActive(true);

        DoctorDepartment dm= doctorDepartmentRepository.findById(request.getDoctorDepatrmentId())
                .orElseThrow(() -> new RuntimeException("Doctor Department not Found"));


        User savedUser = userRepository.save(user);

        if (request == null) return null;

        Doctor doctor = doctorMapper.toEntity(request);
        // Link User
        doctor.setUser(savedUser);

        // Link Department
        doctor.setDoctorDepartment(dm);

        return doctorMapper.toResponse(doctorRepository.save(doctor));
    }

    @Override
    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
        return doctorMapper.toResponse(doctor);
    }

    @Override
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctorMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponse> getDoctorsBySpecialization(String specialization) {
        // Fallback or secondary query filtering
        return doctorRepository.findAll().stream()
                .filter(doc -> doc.getSpecialization() != null && doc.getSpecialization().equalsIgnoreCase(specialization))
                .map(doctorMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Critical Method: Fetches doctors under a specific department selected on the home page.
     */
    @Override

    public List<DoctorResponse> getDoctorsByDepartment(Long departmentId) {
        return doctorRepository.findByDoctorDepartmentId(departmentId).stream()
                .map(doctorMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DoctorResponse updateDoctor(Long id, DoctorRequest request) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        doctorMapper.updateEntityFromRequest(request, doctor);

        doctor.getUser().setName(request.getName());
        doctor.getUser().setEmail(request.getEmail());
        doctor.getUser().setPhone(request.getPhone());

        if(request.getPassword()!=null){
            doctor.getUser().setPassword(passwordEncoder.encode(request.getPassword()));
        }

        DoctorDepartment department =
                doctorDepartmentRepository.findById(request.getDoctorDepatrmentId())
                        .orElseThrow(() -> new RuntimeException("Department not found"));

        doctor.setDoctorDepartment(department);

        userRepository.save(doctor.getUser());

        return doctorMapper.toResponse(doctorRepository.save(doctor));
    }
//    public DoctorResponse updateDoctor(Long id, DoctorRequest request) {
//        Doctor doctor = doctorRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
//
//        doctorMapper.updateEntityFromRequest(request, doctor);
//        return doctorMapper.toResponse(doctorRepository.save(doctor));
//    }

    @Override
    @Transactional
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
        doctorRepository.delete(doctor);
    }


    @Override
    public DoctorResponse getByUserId(Long id) {

        Doctor c = doctorRepository.findByUserId(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return DoctorMapper.toDto(c);
    }

}
