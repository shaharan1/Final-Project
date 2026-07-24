package emranhss.com.Modern_Hospital_Management_System.entity;

import emranhss.com.Modern_Hospital_Management_System.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "office_staff")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfficeStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 80, nullable = false)
    private String name;

    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String position;

    private String age;

    private String department;

    private String workingHours;

    @Temporal(TemporalType.DATE)
    private Date joinDate;

    private String photo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}