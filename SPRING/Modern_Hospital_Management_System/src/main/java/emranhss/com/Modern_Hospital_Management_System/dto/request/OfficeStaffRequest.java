package emranhss.com.Modern_Hospital_Management_System.dto.request;

import emranhss.com.Modern_Hospital_Management_System.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfficeStaffRequest {

    private String name;

    private String email;

    private String phone;

    private String password;

    private Gender gender;

    private String position;

    private String age;

    private String department;

    private String workingHours;

    private Date joinDate;

    private String photo;

}