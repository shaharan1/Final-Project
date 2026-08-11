package emranhss.com.Modern_Hospital_Management_System.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorChargeRequest {



    private String description;

    private double amount;

    private LocalDate visitDate;

    private Long doctorId;

    private Long bedBookingId;

    private Long admittedPatientId;
}
