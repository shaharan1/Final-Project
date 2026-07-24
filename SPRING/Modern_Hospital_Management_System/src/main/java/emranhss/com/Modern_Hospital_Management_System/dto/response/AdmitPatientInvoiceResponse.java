package emranhss.com.Modern_Hospital_Management_System.dto.response;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdmitPatientInvoiceResponse {

    private Long id;
    private String invoiceNumber;
    private Long admittedPatientId;
    private String patientName;
    private String patientCode;
    private double bedCharges;
    private double doctorCharges;
    private double testCharges;
    private double medicineCharges;
    private double mealCharges;
    private double otherCharges;
    private double totalAmount;
    private double tax;
    private double discount;
    private double netPayable;
    private double paidAmount;
    private double dueAmount;
    private String paymentStatus;
    private String generatedBy;
    private LocalDateTime createdDate;

}
