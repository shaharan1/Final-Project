package emranhss.com.Modern_Hospital_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import emranhss.com.Modern_Hospital_Management_System.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "lab_reports")
@NoArgsConstructor
@AllArgsConstructor
public class LabReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 50)
    private String reportNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_order_id", unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Tests testOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ReportStatus reportStatus = ReportStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String finalImpression;

    @Column(columnDefinition = "TEXT")
    private String recommendation;

    private String specialistName;

    private String specialistDesignation;

    @Column(columnDefinition = "LONGTEXT")
    private String specialistSignature;

    private String createdBy;

    private LocalDateTime createdDate;

    private LocalDateTime reportedDate;

    @OneToMany(mappedBy = "labReport", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC, id ASC")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "labReport"})
    private List<LabReportResult> results = new ArrayList<>();
}
