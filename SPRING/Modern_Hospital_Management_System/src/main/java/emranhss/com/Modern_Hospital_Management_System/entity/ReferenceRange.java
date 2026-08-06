package emranhss.com.Modern_Hospital_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "reference_ranges")
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceRange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_parameter_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "referenceRanges", "interpretationRules"})
    private TestParameter testParameter;

    @Column(length = 20)
    private String genderScope = "ANY";

    private Integer minAgeYears;

    private Integer maxAgeYears;

    private Double minValue;

    private Double maxValue;

    private Double criticalLow;

    private Double criticalHigh;

    @Column(length = 100)
    private String displayRange;

    private Integer priority = 0;

    private Boolean active = true;
}
