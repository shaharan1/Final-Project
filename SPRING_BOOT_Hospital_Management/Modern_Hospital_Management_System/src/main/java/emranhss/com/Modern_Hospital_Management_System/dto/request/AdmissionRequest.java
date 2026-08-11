package emranhss.com.Modern_Hospital_Management_System.dto.request;


import lombok.Data;

@Data
public class AdmissionRequest {


    private Long patientId;
    private Long doctorId;
    private Long bedId;
    private String initialDiagnosis;
}
