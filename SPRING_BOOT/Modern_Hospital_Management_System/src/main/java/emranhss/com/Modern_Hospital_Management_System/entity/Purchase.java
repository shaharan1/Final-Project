package emranhss.com.Modern_Hospital_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "purchases")
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Supplier supplier;

    @Column(nullable = false, unique = true)
    private String invoiceNo;

    private LocalDateTime purchaseDate;

    private Double totalAmount = 0.0;

    private Double vat = 0.0;

    private Double discount = 0.0;

    private Double netAmount = 0.0;

    private Double paidAmount = 0.0;

    private Double dueAmount = 0.0;

    @Column(nullable = false)
    private String status = "PENDING";

    private String paymentStatus = "PENDING";

    private String paymentMethod = "CASH";

    private String notes;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("purchase")
    private List<PurchaseItem> items = new ArrayList<>();
}
