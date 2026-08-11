package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.Data;

@Data
public class EmergencyRequest {


    private Long patientId;
    private String severityLevel;
    private String chiefComplaint;
    private String broughtBy;
    private String contactNumber;
}
