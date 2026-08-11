package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class InvoiceRequest {



    private String patientName;
    private String patientContact;
    private Double amount;
    private Double discount;
    private LocalDate deliveryDate;
    private Integer deliveryTime;
    private Double totalAmount;
    private Double totalDiscount;
    private Double payable;
    private Double received;
    private Double due;
    private String preparedBy;
    private Long doctorId;
    private List<Long> testOrderIds; // References to active Tests entries compiled here
}
