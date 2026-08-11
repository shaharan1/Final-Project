package emranhss.com.Modern_Hospital_Management_System.service.lab;

import emranhss.com.Modern_Hospital_Management_System.entity.ReferenceRange;
import emranhss.com.Modern_Hospital_Management_System.enums.ParameterStatus;
import lombok.Data;

@Data
public class InterpretationResult {
    private ParameterStatus status;
    private String interpretation;
    private ReferenceRange referenceRange;
}
