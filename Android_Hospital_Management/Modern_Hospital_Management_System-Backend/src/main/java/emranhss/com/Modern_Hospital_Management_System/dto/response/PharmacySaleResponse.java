package emranhss.com.Modern_Hospital_Management_System.dto.response;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class PharmacySaleResponse {
    private Long id;
    private String saleInvoiceNo;
    private String patientType;
    private String patientName;
    private String patientPhone;
    private Long patientId;
    private Long doctorId;
    private String doctorName;
    private Long prescriptionId;
    private Double totalAmount;
    private Double discount;
    private Double vat;
    private Double netPayable;
    private Double paidAmount;
    private Double changeAmount;
    private String paymentMethod;
    private String paymentStatus;
    private String saleType;
    private Long billingId;
    private List<PharmacySaleItemResponse> items;
    private LocalDateTime saleDate;
}
