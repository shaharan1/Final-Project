package emranhss.com.Modern_Hospital_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "emergency_patients")
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyPatient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String emergencyNumber;

    private String patientName;

    private Integer age;

    private String gender;

    private String phone;

    private String nationalId;

    private String bloodGroup;

    @Column(columnDefinition = "TEXT")
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Patient patient;

    private Boolean isUnknownPatient = false;

    private Boolean isPoliceCase = false;

    private Boolean isReferral = false;

    private String referralHospital;

    private String ambulanceId;

    private String emergencyContactName;

    private String emergencyContactPhone;

    private String emergencyContactRelation;

    @Column(columnDefinition = "TEXT")
    private String symptoms;

    @Column(columnDefinition = "TEXT")
    private String chiefComplaint;

    @Column(columnDefinition = "TEXT")
    private String injuryDetails;

    private String accidentType;

    @Column(columnDefinition = "TEXT")
    private String emergencyNotes;

    @Column(nullable = false)
    private String severityLevel;

    @Column(nullable = false)
    private String status;

    private Integer triageLevel;

    private LocalDateTime arrivalTime;

    private LocalDateTime triageTime;

    private LocalDateTime doctorAssignedTime;

    private LocalDateTime dischargeTime;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
