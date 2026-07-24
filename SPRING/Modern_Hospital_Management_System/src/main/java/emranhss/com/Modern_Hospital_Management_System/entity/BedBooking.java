package emranhss.com.Modern_Hospital_Management_System.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bed_bookings")
@NoArgsConstructor
@AllArgsConstructor
public class BedBooking {




    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admitted_patient_id")
    private AdmittedPatient admittedPatient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emergency_patient_id")
    private EmergencyPatient emergencyPatient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bed_id", nullable = false)
    private Bed bed;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime; // Filled when the bed is vacated

    @Column(nullable = false)
    private Boolean active = true;
}
