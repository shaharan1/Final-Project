package emranhss.com.Modern_Hospital_Management_System.dto.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequest {



    private String name;
    private String gender;
    private LocalDate dateOfBirth;
    private String bloodGroup;
    private String maritalStatus;
    private String phone;
    private String alternatePhone;
    @Email
    private String email;
    private String nationalId;
    private String address;
    private String city;
    private String district;
    private String postalCode;
    private String emergencyContactName;
    private String emergencyContactNumber;
    private String relationship;

    private Long appointmentId;



}
