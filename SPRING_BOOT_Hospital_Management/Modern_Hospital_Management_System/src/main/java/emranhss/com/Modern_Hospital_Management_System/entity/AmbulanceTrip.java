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
@Table(name = "ambulance_trips")
@NoArgsConstructor
@AllArgsConstructor
public class AmbulanceTrip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ambulance_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Ambulance ambulance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emergency_patient_id", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private EmergencyPatient emergencyPatient;

    private String tripType;

    private String pickupLocation;

    private String dropoffLocation;

    private LocalDateTime dispatchTime;

    private LocalDateTime arrivalTime;

    private LocalDateTime completionTime;

    private Double distanceTravelled;

    private Double responseTimeMinutes;

    @Column(nullable = false)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
