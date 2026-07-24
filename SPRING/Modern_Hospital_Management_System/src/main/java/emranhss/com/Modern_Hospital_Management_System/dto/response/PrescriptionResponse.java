package emranhss.com.Modern_Hospital_Management_System.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionResponse {


    private Long id;
    private Long appointmentId;
    private String doctorName;
    private String doctorSpecialization;
    private String patientName;
    private String patientAge;
    private String diagnosis;
    private String chiefComplaints;
    private String symptoms;
    private String bloodPressure;
    private String pulseRate;
    private String bodyTemperature;
    private String weight;
    private String notes;
    private LocalDate nextFollowUpDate;
    private LocalDateTime createdDate;
    private List<PrescriptionItemResponse> prescriptionItems;


    private String doctorPhoto;
    private String doctorDesignation;
    private String doctorDepartment;

    private String patientGender;
    private String patientPhone;
    private String patientBloodGroup;

    private List<TestMasterResponse> tests;

    private String prescriptionNumber;
}
