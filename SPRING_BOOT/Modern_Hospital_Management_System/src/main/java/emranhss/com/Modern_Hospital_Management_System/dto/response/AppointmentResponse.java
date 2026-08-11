package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {


    private Long id;
    private String appointmentNumber;
    private String status;

    // Form parameters
    private String patientName;
    private String mobileNumber;
    private String specialization;
//    private String name;
//    private String phone;
    private String problemDescription;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;


    private Double feeCharged;

    // Payment fields
    private String paymentMethod;
    private String transactionId;

    // Relationship IDs
    private Long registeredPatientId;
    private Long doctorId;
    private String doctorName;
    private String doctorChamber;
    private Long scheduleSlotId;
    private Boolean slotIsBooked;


    private String doctorSpecialization;
}
