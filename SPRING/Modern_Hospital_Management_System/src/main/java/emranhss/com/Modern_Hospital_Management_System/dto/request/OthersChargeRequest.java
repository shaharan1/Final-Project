package emranhss.com.Modern_Hospital_Management_System.dto.request;


import lombok.Data;

@Data
public class OthersChargeRequest {


    private String category;
    private String description;
    private double unitPrice;
    private Integer quantity;
    private Long bedBookingId;
    private Long admittedPatientId;
    private String enteredBy;
}
