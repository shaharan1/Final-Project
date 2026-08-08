package emranhss.com.Modern_Hospital_Management_System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "surgery_masters")
@NoArgsConstructor
@AllArgsConstructor
public class SurgeryMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String surgeryCode;

    @Column(nullable = false, length = 150)
    private String surgeryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private SurgeryCategory category;

    @Column(nullable = false)
    private Double standardRate = 0.0;

    private Double otCharge = 0.0;
    private Double surgeonFee = 0.0;
    private Double assistantSurgeonFee = 0.0;
    private Double anesthesiaCharge = 0.0;
    private Double nursingCharge = 0.0;
    private Double equipmentCharge = 0.0;
    private Double consumableCharge = 0.0;
    private Double icuCharge = 0.0;
    private Double wardCabinCharge = 0.0;
    private Double medicineCharge = 0.0;
    private Double laboratoryCharge = 0.0;
    private Double radiologyCharge = 0.0;
    private Double packageRate = 0.0;

    @Column(nullable = false)
    private Boolean active = true;

    private Integer estimatedDurationMin = 60;

    @Column(length = 500)
    private String notes;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime lastUpdated;
}
