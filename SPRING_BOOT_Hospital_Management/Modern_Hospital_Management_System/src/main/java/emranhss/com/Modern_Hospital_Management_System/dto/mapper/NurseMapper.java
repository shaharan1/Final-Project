package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.NurseRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.NurseResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Nurse;
import emranhss.com.Modern_Hospital_Management_System.entity.User;
import org.springframework.stereotype.Component;

@Component
public class NurseMapper {

    public Nurse toEntity(NurseRequest request) {
        if (request == null) return null;
        User u = new User();

        u.setName(request.getName());
        u.setEmail(request.getEmail());
        u.setPhone(request.getPhone());
        u.setPassword(request.getPassword());

        Nurse nurse = new Nurse();
        nurse.setUser(u);

        nurse.setAddress(request.getAddress());
        nurse.setGender(request.getGender());
        nurse.setJoinDate(request.getJoinDate());
        nurse.setPhoto(request.getPhoto());
        nurse.setNurseType(request.getNurseType());
        nurse.setQualification(request.getQualification());
        nurse.setRegistrationNumber(request.getRegistrationNumber());
        nurse.setExperienceYears(request.getExperienceYears());
        nurse.setShift(request.getShift());
        nurse.setWorkingHours(request.getWorkingHours());
        nurse.setOnDuty(request.getOnDuty() != null ? request.getOnDuty() : false);
        nurse.setAssignedWard(request.getAssignedWard());
        nurse.setRemarks(request.getRemarks());
        nurse.setActive(true);
        return nurse;
    }

    public NurseResponse toResponse(Nurse nurse) {
        if (nurse == null) return null;
        return NurseResponse.builder()
                .id(nurse.getId())
                .userId(nurse.getUser() != null ? nurse.getUser().getId() : 0)
                .name(nurse.getUser().getName())
                .email(nurse.getUser().getEmail())
                .phone(nurse.getUser().getPhone())
                .address(nurse.getAddress())
                .gender(nurse.getGender())
                .joinDate(nurse.getJoinDate())
                .photo(nurse.getPhoto())
                .nurseType(nurse.getNurseType())
                .qualification(nurse.getQualification())
                .registrationNumber(nurse.getRegistrationNumber())
                .experienceYears(nurse.getExperienceYears())
                .shift(nurse.getShift())
                .workingHours(nurse.getWorkingHours())
                .onDuty(nurse.getOnDuty())
                .assignedWard(nurse.getAssignedWard())
                .remarks(nurse.getRemarks())
                .active(nurse.getActive())
                .build();
    }
}
