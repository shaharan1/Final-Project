package emranhss.com.Modern_Hospital_Management_System.dto.response;


import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class InvoiceResponse {



    private Long id;
    private String patientName;
    private String patientContact;
    private Double amount;
    private Double discount;
    private LocalDate invoiceDate;
    private LocalDate deliveryDate;
    private Integer deliveryTime;
    private Double totalAmount;
    private Double totalDiscount;
    private Double payable;
    private Double received;
    private Double due;
    private Boolean status;
    private String preparedBy;
    private String referredDoctorName;
    private List<InpatientTestResponse> compiledTests;
}
