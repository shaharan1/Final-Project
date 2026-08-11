package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TriageRequest {

    private Long emergencyPatientId;
    private Integer triageLevel;
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
}
