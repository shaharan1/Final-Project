package emranhss.com.Modern_Hospital_Management_System.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "invoices")
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String patientName;

    @Column(nullable = false)
    private String patientContact;

    private Double amount;
    private Double discount;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate invoiceDate;

    private LocalDate deliveryDate;
    private Integer deliveryTime; //  Stored in hours or specific slot markers

    private Double totalAmount;
    private Double totalDiscount;
    private Double payable;
    private Double received;
    private Double due;

    private Boolean status = false; // false = UNPAID/PARTIAL, true = PAID
    private String preparedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "invoice_id")
    private List<Tests> tests = new ArrayList<>();
}
