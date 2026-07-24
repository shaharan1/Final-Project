package emranhss.com.Modern_Hospital_Management_System.dto.request;


import lombok.Data;

@Data
public class InpatientTestRequest {


    private Long testMasterId;
    private Long patientId;
    private Long doctorId;
    private Long admittedPatientId;
}
