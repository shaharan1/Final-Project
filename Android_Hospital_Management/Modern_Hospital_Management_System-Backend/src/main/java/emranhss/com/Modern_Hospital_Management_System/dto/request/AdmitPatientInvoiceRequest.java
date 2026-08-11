package emranhss.com.Modern_Hospital_Management_System.dto.request;


import lombok.Data;

@Data
public class AdmitPatientInvoiceRequest {


    private Long admittedPatientId;
    private double discount;
    private double paidAmount;
    private String generatedBy;
}
