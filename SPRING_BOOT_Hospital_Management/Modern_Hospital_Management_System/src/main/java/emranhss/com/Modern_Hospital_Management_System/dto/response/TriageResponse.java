package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TriageResponse {

    private Long id;
    private Long emergencyPatientId;
    private String emergencyNumber;
    private Integer triageLevel;
    private String triageColor;
    private Integer bloodPressureSystolic;
    private Integer bloodPressureDiastolic;
    private Integer pulse;
    private Double temperature;
    private Integer oxygenSaturation;
    private Integer respirationRate;
    private Integer painScore;
    private Integer glasgowComaScale;
    private String assessmentNotes;
    private String assessedBy;
    private LocalDateTime assessedAt;
}
