package emranhss.com.Modern_Hospital_Management_System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "lab_rules")
@NoArgsConstructor
@AllArgsConstructor
public class LabRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String ruleCode;

    @Column(nullable = false, length = 150)
    private String ruleName;

    @Column(columnDefinition = "TEXT")
    private String conditions;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String finalImpression;

    @Column(columnDefinition = "TEXT")
    private String recommendation;

    private Integer priority = 0;

    private Boolean active = true;
}
