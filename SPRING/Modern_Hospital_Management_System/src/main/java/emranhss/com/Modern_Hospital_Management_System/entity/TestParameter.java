package emranhss.com.Modern_Hospital_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import emranhss.com.Modern_Hospital_Management_System.enums.ResultType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "test_parameters")
@NoArgsConstructor
@AllArgsConstructor
public class TestParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_master_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private TestMaster testMaster;

    @Column(nullable = false, length = 100)
    private String parameterName;

    @Column(nullable = false, length = 50)
    private String parameterCode;

    @Column(length = 30)
    private String unit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ResultType resultType = ResultType.NUMERIC;

    @Column(columnDefinition = "TEXT")
    private String allowedValues;

    private Integer displayOrder = 0;

    private Integer decimalPrecision = 1;

    @Column(columnDefinition = "TEXT")
    private String normalText;

    private Boolean active = true;

    @OneToMany(mappedBy = "testParameter", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("priority ASC, id ASC")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "testParameter"})
    private List<ReferenceRange> referenceRanges = new ArrayList<>();

    @OneToMany(mappedBy = "testParameter", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC, id ASC")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "testParameter"})
    private List<InterpretationRule> interpretationRules = new ArrayList<>();
}
