package emranhss.com.Modern_Hospital_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "appointments")
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String appointmentNumber;

    // Guest Info Fields
    private String patientName;
    private String mobileNumber;
    private String specialization;

//    // Core Booking Parameters
//    private String name;
//    private String phone;
    @Column(columnDefinition = "TEXT")
    private String problemDescription;

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    // Payment Gateway Tracking
    private String paymentMethod;
    private String transactionId;
    private String status;             // PENDING, CONFIRMED, CANCELLED
    private LocalDateTime createdDate;
    private Double feeCharged; // Persists either consultationFee or followUpFee

    // Registered Patient Link (Optional, nullable for guest checkouts)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "patient_id", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Doctor doctor;

    // Fixed Link: References the exact reserved schedule slot time boundary
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_slot_id", nullable = false, unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ScheduleSlot scheduleSlot;
}
