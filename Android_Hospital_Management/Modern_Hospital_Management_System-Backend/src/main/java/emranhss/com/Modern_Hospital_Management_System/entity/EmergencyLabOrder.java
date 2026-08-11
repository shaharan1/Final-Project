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
@Table(name = "emergency_lab_orders")
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyLabOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emergency_patient_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private EmergencyPatient emergencyPatient;

    private String testName;

    private String orderType;

    private String orderedBy;

    private LocalDateTime orderedAt;

    private LocalDateTime sampleCollectionTime;

    private LocalDateTime resultTime;

    @Column(columnDefinition = "TEXT")
    private String resultValue;

    @Column(nullable = false)
    private String status;

    private Boolean isCritical = false;

    private String priority;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
