package emranhss.com.Modern_Hospital_Management_System.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRequest {


    private String name;
    private String email;
    private String phone;
    private String password;
    private String gender;
    private String status;
    private String study;
    private String specialization;
    private String designation;
    private String registrationNumber;
    private Integer experienceYears;
    private Double consultationFee;
    private Double followUpFee;
    private String availableDays;
    private String dutyHours;
    private String chamber;
    private LocalDate joinDate;
    private String photo;
    private Long doctorDepatrmentId;


}
