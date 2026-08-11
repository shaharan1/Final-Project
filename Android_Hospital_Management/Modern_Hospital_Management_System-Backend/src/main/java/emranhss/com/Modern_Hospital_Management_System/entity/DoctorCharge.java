package emranhss.com.Modern_Hospital_Management_System.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "doctorCharges")
@NoArgsConstructor
@AllArgsConstructor
public class DoctorCharge {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private double amount;

    private LocalDate visitDate;

    private String billingStatus = "PENDING";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bed_booking_id")
    private BedBooking bedBooking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admitted_patient_id")
    private AdmittedPatient admittedPatient;


}
