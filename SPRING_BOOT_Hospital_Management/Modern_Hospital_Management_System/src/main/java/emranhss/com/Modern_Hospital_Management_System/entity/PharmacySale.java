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
@Table(name = "pharmacy_sales")
@NoArgsConstructor
@AllArgsConstructor
public class PharmacySale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String saleInvoiceNo;

    private String patientType;

    private String patientName;

    private String patientPhone;

    private Long patientId;

    private Long doctorId;

    private String doctorName;

    private Long prescriptionId;

    private Double totalAmount = 0.0;

    private Double discount = 0.0;

    private Double vat = 0.0;

    private Double netPayable = 0.0;

    private Double paidAmount = 0.0;

    private Double changeAmount = 0.0;

    private String paymentMethod = "CASH";

    @Column(nullable = false)
    private String paymentStatus = "PAID";

    private String saleType = "COUNTER";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Billing billing;

    @OneToMany(mappedBy = "pharmacySale", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("pharmacySale")
    private List<PharmacySaleItem> items = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime saleDate;
}
