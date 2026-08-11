package emranhss.com.Modern_Hospital_Management_System.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
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
    private Long departmentId; // Added to map relationship back to front



    private String departmentName;
    private String description;

}
