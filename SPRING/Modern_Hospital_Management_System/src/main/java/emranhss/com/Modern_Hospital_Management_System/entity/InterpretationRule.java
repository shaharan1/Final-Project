package emranhss.com.Modern_Hospital_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "interpretation_rules")
@NoArgsConstructor
@AllArgsConstructor
public class InterpretationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_parameter_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "referenceRanges", "interpretationRules"})
    private TestParameter testParameter;

    @Column(nullable = false, length = 30)
    private String parameterStatus;

    @Column(length = 100)
    private String valueMatch;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String interpretationText;

    private Integer displayOrder = 0;

    private Boolean active = true;
}
