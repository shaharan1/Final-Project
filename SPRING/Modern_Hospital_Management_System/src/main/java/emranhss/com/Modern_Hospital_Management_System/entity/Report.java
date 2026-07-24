package emranhss.com.Modern_Hospital_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Entity
@Table(name = "reports")
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reportResult;
    private String description;
    private String sampleId;
    private String interpretation;
    private String preparedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date testDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryDate;

    // Fixed Link: Connects report to a specific patient row
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Patient patient;

    // Fixed Link: Connects report to a specific doctor row
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Doctor doctor;
}
