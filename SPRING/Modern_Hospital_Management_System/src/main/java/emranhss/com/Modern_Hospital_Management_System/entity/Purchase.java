package emranhss.com.Modern_Hospital_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
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

    @Temporal(TemporalType.DATE)
    private Date purchaseDate;

    private double totalAmount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;


    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("purchase")
    private List<PurchaseItem> items;
}
