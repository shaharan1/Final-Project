package emranhss.com.Modern_Hospital_Management_System.dto.request;
import lombok.Data;
import java.util.List;
@Data
public class PharmacySaleRequest {
    private String patientType;
    private String patientName;
    private String patientPhone;
    private Long patientId;
    private Long doctorId;
    private String doctorName;
    private Long prescriptionId;
    private Long billingId;
    private Double discount;
    private Double vat;
    private Double paidAmount;
    private String paymentMethod;
    private String saleType;
    private List<PharmacySaleItemRequest> items;
}
