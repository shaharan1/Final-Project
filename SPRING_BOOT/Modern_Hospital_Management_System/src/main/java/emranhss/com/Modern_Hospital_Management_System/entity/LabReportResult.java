package emranhss.com.Modern_Hospital_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import emranhss.com.Modern_Hospital_Management_System.enums.ParameterStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "lab_report_results")
@NoArgsConstructor
@AllArgsConstructor
public class LabReportResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_report_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "results"})
    private LabReport labReport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_parameter_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private TestParameter testParameter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_range_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ReferenceRange referenceRange;

    private String parameterName;

    private String parameterCode;

    private String unit;

    private String resultValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ParameterStatus status = ParameterStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String interpretation;

    private Boolean abnormal = false;

    private Boolean critical = false;

    private Integer displayOrder = 0;
}
