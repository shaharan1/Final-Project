package emranhss.com.Modern_Hospital_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "doctors")
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(length = 80, nullable = false)
//    private String name;
//
//    @Column(length = 80, nullable = false, unique = true)
//    private String email;
//
//    @Column(unique = true)
//    private String phone;

    private String gender;
    private String status;
    private String study;

    @Column(nullable = false)
    private String specialization;

    private String designation;

    @Column(unique = true)
    private String registrationNumber;

    private Integer experienceYears;
    private Double consultationFee; // Used as First Visit Rate
    private Double followUpFee;     // Used as Second/Returning Visit Rate
    private String availableDays;
    private String dutyHours;
    private String chamber;
    private LocalDate joinDate;
    private String photo;


    @OneToMany(mappedBy = "doctor")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "slots"})
    private List<ScheduleSlot> slots;


    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_department_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "doctors"})
    private DoctorDepartment doctorDepartment;
}
