package emranhss.com.Modern_Hospital_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "purchase_items")
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "purchaseItems"})
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private MedicineStock medicineStock;

    private int quantity;
    private double unitPrice;
    private double subtotal;
}
