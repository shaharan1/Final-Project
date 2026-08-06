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
@Table(name = "doctor_discounts")
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "user", "slots", "reports"})
    private Doctor doctor;

    @Column(nullable = false)
    private Double fixedDiscount = 0.0;          // Flat Taka discount

    @Column(nullable = false)
    private Double percentageDiscount = 0.0;     // Percent discount on surgery rate

    @Column(nullable = false)
    private Double departmentDiscount = 0.0;     // Department level discount percent

    @Column(nullable = false)
    private Double specialPromoDiscount = 0.0;   // Promotional discount percent

    @Column(nullable = false)
    private Boolean active = true;

    @Column(length = 300)
    private String notes;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime lastUpdated;
}
