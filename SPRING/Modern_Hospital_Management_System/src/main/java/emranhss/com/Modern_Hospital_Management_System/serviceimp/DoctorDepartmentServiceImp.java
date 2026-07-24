package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.DoctorDepartmentMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.DoctorDepartmentRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.DoctorDepartmentResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.DoctorDepartment;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.DoctorDepartmentRepository;
import emranhss.com.Modern_Hospital_Management_System.service.DoctorDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorDepartmentServiceImp implements DoctorDepartmentService {

    private final DoctorDepartmentRepository departmentRepository;
    private final DoctorDepartmentMapper mapper;

    @Override
    @Transactional
    public DoctorDepartmentResponse createDepartment(DoctorDepartmentRequest request) {
        departmentRepository.findByDepartmentNameIgnoreCase(request.getDepartmentName())
                .ifPresent(d -> { throw new IllegalStateException("Department already exists with name: " + request.getDepartmentName()); });

        DoctorDepartment department = mapper.toEntity(request);
        return mapper.toResponse(departmentRepository.save(department));
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorDepartmentResponse getDepartmentById(Long id) {
        DoctorDepartment department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        return mapper.toResponse(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DoctorDepartmentResponse updateDepartment(Long id, DoctorDepartmentRequest request) {
        DoctorDepartment department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));

        department.setDepartmentName(request.getDepartmentName());
        department.setDescription(request.getDescription());

        return mapper.toResponse(departmentRepository.save(department));
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        DoctorDepartment department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        departmentRepository.delete(department);
    }
}
