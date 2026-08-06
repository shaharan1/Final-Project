package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurgeryMasterRequest {

    private String surgeryCode;
    private String surgeryName;
    private Long categoryId;
    private Double standardRate;
    private Double otCharge;
    private Double anesthesiaCharge;
    private Double nursingCharge;
    private Double equipmentCharge;
    private Double consumableCharge;
    private Double icuCharge;
    private Double packageRate;
    private Boolean active;
    private Integer estimatedDurationMin;
    private String notes;
}
