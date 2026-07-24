package emranhss.com.Modern_Hospital_Management_System.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    @Column(nullable = false, length = 50)
    private String batchNumber;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false)
    private Double purchasePrice;

    @Column(nullable = false)
    private Double salePrice;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;


}
