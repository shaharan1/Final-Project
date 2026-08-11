package emranhss.com.Modern_Hospital_Management_System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "medicine_stocks")
@NoArgsConstructor
@AllArgsConstructor
public class MedicineStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String medicineName;

    private String genericName;

    private String strength;

    private String dosageForm;

    @Column(nullable = false, length = 50)
    private String batchNumber;

    @Column(nullable = false)
    private Integer stockQuantity;

    private Integer reservedQuantity = 0;

    private Integer damagedQuantity = 0;

    @Column(nullable = false)
    private Double purchasePrice;

    @Column(nullable = false)
    private Double salePrice;

    private Double vat = 0.0;

    private Integer minimumStockLevel = 10;

    private Integer reorderLevel = 20;

    @Column(nullable = false)
    private LocalDate manufacturingDate;

    @Column(nullable = false)
    private LocalDate expiryDate;

    private String barcode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    public int getAvailableQuantity() {
        return (stockQuantity != null ? stockQuantity : 0)
             - (reservedQuantity != null ? reservedQuantity : 0)
             - (damagedQuantity != null ? damagedQuantity : 0);
    }

    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDate.now());
    }

    public boolean isExpiringSoon(int days) {
        return expiryDate != null && !isExpired()
            && expiryDate.isBefore(LocalDate.now().plusDays(days));
    }

    public boolean isLowStock() {
        return getAvailableQuantity() <= (reorderLevel != null ? reorderLevel : 20);
    }
}
