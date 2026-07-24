package emranhss.com.Modern_Hospital_Management_System.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "suppliers")
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String name;

    private String contactPerson;

    @Column(nullable = false)
    private String phone;

    private String email;

    @Column(columnDefinition = "TEXT")
    private String address;

    private Boolean active = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;



}
