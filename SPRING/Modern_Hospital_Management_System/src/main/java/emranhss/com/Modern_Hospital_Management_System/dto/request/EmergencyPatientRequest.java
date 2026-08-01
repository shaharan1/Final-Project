package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyPatientRequest {

    private Long patientId;
    private String patientName;
    private Integer age;
    private String gender;
    private String phone;
    private String nationalId;
    private String bloodGroup;
    private String address;
    private Boolean isUnknownPatient;
    private Boolean isPoliceCase;
    private Boolean isReferral;
    private String referralHospital;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelation;
    private String symptoms;
    private String chiefComplaint;
    private String injuryDetails;
    private String accidentType;
    private String emergencyNotes;
    private String ambulanceId;
    private String severityLevel;
    private String status;
    private Integer triageLevel;
}
