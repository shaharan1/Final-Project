package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.OfficeStaffMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.OfficeStaffRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.OfficeStaffResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.OfficeStaff;
import emranhss.com.Modern_Hospital_Management_System.entity.User;
import emranhss.com.Modern_Hospital_Management_System.enums.Role;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.OfficeStaffRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.UserRepository;
import emranhss.com.Modern_Hospital_Management_System.service.OfficeStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfficeStaffServiceImp implements OfficeStaffService {

    private final OfficeStaffRepository officeStaffRepository;
    private final OfficeStaffMapper officeStaffMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OfficeStaffResponse createOfficeStaff(OfficeStaffRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword());
        user.setRole(Role.OfficeStaff);
        user.setActive(true);

        User savedUser = userRepository.save(user);

        OfficeStaff officeStaff = officeStaffMapper.toEntity(request);

        officeStaff.setUser(savedUser);

        return officeStaffMapper.toResponse(
                officeStaffRepository.save(officeStaff)
        );
    }

    @Override
    public OfficeStaffResponse getOfficeStaffById(Long id) {

        OfficeStaff officeStaff = officeStaffRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Office Staff not found with id : " + id));

        return officeStaffMapper.toResponse(officeStaff);
    }

    @Override
    public List<OfficeStaffResponse> getAllOfficeStaff() {

        return officeStaffRepository.findAll()
                .stream()
                .map(officeStaffMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OfficeStaffResponse> getOfficeStaffByDepartment(String department) {

        return officeStaffRepository.findByDepartment(department)
                .stream()
                .map(officeStaffMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OfficeStaffResponse> getOfficeStaffByPosition(String position) {

        return officeStaffRepository.findByPosition(position)
                .stream()
                .map(officeStaffMapper::toResponse)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public OfficeStaffResponse updateOfficeStaff(Long id, OfficeStaffRequest request) {

        OfficeStaff officeStaff = officeStaffRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Office Staff not found with id : " + id));

        officeStaffMapper.updateEntityFromRequest(request, officeStaff);

        // Update User Information
        officeStaff.getUser().setName(request.getName());
        officeStaff.getUser().setEmail(request.getEmail());
        officeStaff.getUser().setPhone(request.getPhone());

        if (request.getPassword() != null &&
                !request.getPassword().trim().isEmpty()) {

            officeStaff.getUser().setPassword(request.getPassword());
        }

        userRepository.save(officeStaff.getUser());

        OfficeStaff updatedOfficeStaff =
                officeStaffRepository.save(officeStaff);

        return officeStaffMapper.toResponse(updatedOfficeStaff);
    }

    @Override
    @Transactional
    public void deleteOfficeStaff(Long id) {

        OfficeStaff officeStaff = officeStaffRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Office Staff not found with id : " + id));

        userRepository.delete(officeStaff.getUser());

        officeStaffRepository.delete(officeStaff);
    }

}