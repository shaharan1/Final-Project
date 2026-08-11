package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyPatientResponse {

    private Long id;
    private String emergencyNumber;
    private String patientName;
    private Integer age;
    private String gender;
    private String phone;
    private String nationalId;
    private String bloodGroup;
    private String address;
    private Long patientId;
    private Boolean isUnknownPatient;
    private Boolean isPoliceCase;
    private Boolean isReferral;
    private String referralHospital;
    private String ambulanceId;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelation;
    private String symptoms;
    private String chiefComplaint;
    private String injuryDetails;
    private String accidentType;
    private String emergencyNotes;
    private String severityLevel;
    private String status;
    private Integer triageLevel;
    private LocalDateTime arrivalTime;
    private LocalDateTime triageTime;
    private LocalDateTime doctorAssignedTime;
    private LocalDateTime dischargeTime;
    private LocalDateTime createdAt;
}
