package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.NurseMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.NurseRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.NurseResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Nurse;
import emranhss.com.Modern_Hospital_Management_System.entity.User;
import emranhss.com.Modern_Hospital_Management_System.enums.Role;
import emranhss.com.Modern_Hospital_Management_System.repository.NurseRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.UserRepository;
import emranhss.com.Modern_Hospital_Management_System.service.NurseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NurseServiceImpl implements NurseService {

    private final NurseRepository nurseRepository;
    private final UserRepository userRepository;
    private final NurseMapper nurseMapper;



    @Override
    @Transactional
    public NurseResponse saveNurseProfile(NurseRequest request) {

        User user = new User();

        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword());
        user.setRole(Role.Nurse);
        user.setActive(true);



        if (nurseRepository.findByUserEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("A clinical profile with this email already exists.");
        }

        User savedUser = userRepository.save(user);


        Nurse nurse = nurseMapper.toEntity(request);
        nurse.setUser(savedUser);

        Nurse savedNurse = nurseRepository.save(nurse);
        return nurseMapper.toResponse(savedNurse);
    }

    @Override
    public NurseResponse getNurseById(Long id) {
        Nurse nurse = nurseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nurse medical profile record not found for ID: " + id));
        return nurseMapper.toResponse(nurse);
    }

    @Override
    public NurseResponse getNurseByEmail(String email) {
        Nurse nurse = nurseRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Nurse medical profile record not found for email: " + email));
        return nurseMapper.toResponse(nurse);
    }

    @Override
    public List<NurseResponse> getAllActiveNurses() {
        return nurseRepository.findAll().stream()
                .filter(Nurse::getActive)
                .map(nurseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NurseResponse> getNursesByWard(String wardName) {
        return nurseRepository.findByAssignedWardAndActiveTrue(wardName).stream()
                .map(nurseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NurseResponse> getOnDutyNurses() {
        return nurseRepository.findByOnDutyTrueAndActiveTrue().stream()
                .map(nurseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NurseResponse updateNurseDutyStatus(Long id, boolean onDuty) {
        Nurse nurse = nurseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nurse profile records not found."));
        nurse.setOnDuty(onDuty);
        return nurseMapper.toResponse(nurseRepository.save(nurse));
    }

    @Override
    @Transactional
    public NurseResponse toggleNurseActiveStatus(Long id, boolean active) {
        Nurse nurse = nurseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nurse profile records not found."));
        nurse.setActive(active);
        return nurseMapper.toResponse(nurseRepository.save(nurse));
    }
}
