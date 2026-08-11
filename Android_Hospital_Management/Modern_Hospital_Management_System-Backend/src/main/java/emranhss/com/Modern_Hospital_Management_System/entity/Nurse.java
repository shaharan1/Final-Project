package emranhss.com.Modern_Hospital_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import emranhss.com.Modern_Hospital_Management_System.enums.Gender;
import emranhss.com.Modern_Hospital_Management_System.enums.NurseType;
import emranhss.com.Modern_Hospital_Management_System.enums.ShiftType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nurses")
public class Nurse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //---------------------------------------
    // User Login Information
    //---------------------------------------

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    //---------------------------------------
    // Personal Information
    //---------------------------------------

//    @Column(nullable = false, length = 80)
//    private String fullName;
//
//    @Column(unique = true)
//    private String email;
//
//    @Column(length = 20)
//    private String phone;


    @Column(length = 300)
    private String address;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate joinDate;

    private String photo;

    //---------------------------------------
    // Professional Information
    //---------------------------------------

    @Enumerated(EnumType.STRING)
    private NurseType nurseType;

    private String qualification;

    private String registrationNumber;

    private Integer experienceYears;

    //---------------------------------------
    // Duty Information
    //---------------------------------------

    @Enumerated(EnumType.STRING)
    private ShiftType shift;

    private String workingHours;

    private Boolean onDuty;

    //---------------------------------------
    // Hospital Assignment
    //---------------------------------------

    private String assignedWard;

    private String remarks;

    //---------------------------------------
    // Status
    //---------------------------------------


    private Boolean active = true;

}
