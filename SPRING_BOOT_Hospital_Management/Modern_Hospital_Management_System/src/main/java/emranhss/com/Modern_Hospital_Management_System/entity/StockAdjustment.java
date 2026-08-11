package emranhss.com.Modern_Hospital_Management_System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "stock_adjustments")
@NoArgsConstructor
@AllArgsConstructor
public class StockAdjustment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_stock_id", nullable = false)
    private MedicineStock medicineStock;

    @Column(nullable = false)
    private String adjustmentType;

    private Integer quantityChange;

    private Integer previousQuantity;

    private Integer newQuantity;

    private String reason;

    private String performedBy;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime adjustedAt;
}
