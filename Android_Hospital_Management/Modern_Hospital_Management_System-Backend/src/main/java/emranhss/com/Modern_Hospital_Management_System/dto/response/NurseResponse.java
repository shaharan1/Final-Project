package emranhss.com.Modern_Hospital_Management_System.dto.response;

import emranhss.com.Modern_Hospital_Management_System.enums.Gender;
import emranhss.com.Modern_Hospital_Management_System.enums.NurseType;
import emranhss.com.Modern_Hospital_Management_System.enums.ShiftType;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class NurseResponse {

    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Gender gender;
    private LocalDate joinDate;
    private String photo;
    private NurseType nurseType;
    private String qualification;
    private String registrationNumber;
    private Integer experienceYears;
    private ShiftType shift;
    private String workingHours;
    private Boolean onDuty;
    private String assignedWard;
    private String remarks;
    private Boolean active;
}
