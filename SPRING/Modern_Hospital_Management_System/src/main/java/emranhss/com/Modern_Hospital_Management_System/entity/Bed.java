package emranhss.com.Modern_Hospital_Management_System.entity;


import emranhss.com.Modern_Hospital_Management_System.enums.BedStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "beds")
@NoArgsConstructor
@AllArgsConstructor
public class Bed {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String bedNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id", nullable = false)
    private Ward ward;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BedStatus status = BedStatus.AVAILABLE;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "bed_facilities",
            joinColumns = @JoinColumn(name = "bed_id"),
            inverseJoinColumns = @JoinColumn(name = "facility_id")
    )
    private Set<Facility> facilities = new HashSet<>();
}
