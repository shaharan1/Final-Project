package emranhss.com.Modern_Hospital_Management_System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "operation_theatres")
@NoArgsConstructor
@AllArgsConstructor
public class OperationTheatre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String otCode;

    @Column(nullable = false, length = 100)
    private String otName;

    @Column(length = 150)
    private String location;

    @Column(length = 500)
    private String equipmentAvailable;

    private Integer capacity = 1;

    @Column(length = 20)
    private String status = "AVAILABLE"; // AVAILABLE, IN_USE, UNDER_MAINTENANCE, RESERVED

    @Column(nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime lastUpdated;
}
