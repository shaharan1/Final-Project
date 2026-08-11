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
@Table(name = "emergency_triage")
@NoArgsConstructor
@AllArgsConstructor
public class Triage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emergency_patient_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private EmergencyPatient emergencyPatient;

    private Integer triageLevel;

    private String triageColor;

    private Integer bloodPressureSystolic;

    private Integer bloodPressureDiastolic;

    private Integer pulse;

    private Double temperature;

    private Integer oxygenSaturation;

    private Integer respirationRate;

    private Integer painScore;

    private Integer glasgowComaScale;

    @Column(columnDefinition = "TEXT")
    private String assessmentNotes;

    private String assessedBy;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime assessedAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
