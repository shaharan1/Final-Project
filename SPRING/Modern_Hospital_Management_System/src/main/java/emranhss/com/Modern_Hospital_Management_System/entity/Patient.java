package emranhss.com.Modern_Hospital_Management_System.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "patients")
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String patientCode;

    private String name;
    private String gender;
    private LocalDate dateOfBirth;
    private String bloodGroup;
    private String maritalStatus;

    @Column(unique = true)
    private String phone;

    private String alternatePhone;

    @Column(unique = true,nullable = true)
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
