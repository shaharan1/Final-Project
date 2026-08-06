package emranhss.com.Modern_Hospital_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "abnormal_alerts")
@NoArgsConstructor
@AllArgsConstructor
public class AbnormalAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Patient patient;

    private Long labReportId;

    private String parameterName;

    private String resultValue;

    @Column(length = 30)
    private String status;

    @Column(length = 30)
    private String severity;

    private Boolean resolved = false;

    @CreationTimestamp
    private LocalDateTime createdDate;
}
