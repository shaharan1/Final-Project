package emranhss.com.Modern_Hospital_Management_System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ambulances")
@NoArgsConstructor
@AllArgsConstructor
public class Ambulance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String ambulanceNumber;

    private String vehicleType;

    private String vehiclePlate;

    private String driverName;

    private String driverPhone;

    private String paramedicName;

    private String paramedicPhone;

    @Column(nullable = false)
    private String status;

    private String currentLocation;

    private Integer fuelStatus;

    private LocalDate lastServiceDate;

    private LocalDate nextServiceDate;

    private Boolean isActive = true;

    @Column(columnDefinition = "TEXT")
    private String equipmentChecklist;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
