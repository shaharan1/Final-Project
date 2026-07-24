package emranhss.com.Modern_Hospital_Management_System.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorChargeResponse {


    private Long id;

    private String description;

    private double amount;

    private LocalDate visitDate;

    private Long doctorId;

    private String doctorName;

    private Long bedBookingId;

    private Long admittedPatientId;

    private String patientName;

    private String admissionStatus;

    private String billingStatus;
}
