package emranhss.com.Modern_Hospital_Management_System.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionRequest {


    private Long appointmentId;
    private Long doctorId;
    private Long patientId;
    private String diagnosis;
    private String chiefComplaints;
    private String symptoms;
    private String bloodPressure;
    private String pulseRate;
    private String bodyTemperature;
    private String weight;
    private String notes;
    private LocalDate nextFollowUpDate;
    private List<PrescriptionItemRequest> prescriptionItems;

    private List<Long> testIds;
}
