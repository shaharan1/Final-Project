package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;




@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {

    private Long id;
    private String patientCode;
    private String name;
    private String gender;
    private LocalDate dateOfBirth;
    private String bloodGroup;
    private String maritalStatus;
    private String phone;
    private String alternatePhone;
    private String email;
    private String nationalId;
    private String address;
    private String city;
    private String district;
    private String postalCode;
    private String emergencyContactName;
    private String emergencyContactNumber;
    private String relationship;


}
