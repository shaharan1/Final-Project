package emranhss.com.Modern_Hospital_Management_System.dto.response;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OthersChargeResponse {

    private Long id;
    private String category;
    private String description;
    private double unitPrice;
    private Integer quantity;
    private double amount;
    private Long bedBookingId;
    private String bedNumber;
    private Long admittedPatientId;
    private String patientName;
    private String billingStatus;
    private String enteredBy;
    private LocalDateTime createdDate;
}
