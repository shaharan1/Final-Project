package emranhss.com.Modern_Hospital_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "pharmacy_sale_items")
@NoArgsConstructor
@AllArgsConstructor
public class PharmacySaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacy_sale_id", nullable = false)
    @JsonIgnoreProperties("saleItems")
    private PharmacySale pharmacySale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_stock_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private MedicineStock medicineStock;

    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;
}
