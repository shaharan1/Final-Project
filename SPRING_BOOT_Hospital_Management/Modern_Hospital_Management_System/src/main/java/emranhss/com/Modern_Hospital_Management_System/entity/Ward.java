package emranhss.com.Modern_Hospital_Management_System.entity;


import emranhss.com.Modern_Hospital_Management_System.enums.RoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "wards")
@NoArgsConstructor
@AllArgsConstructor
public class Ward {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    private Integer totalBeds;

    private Double basePricePerDay;
}
