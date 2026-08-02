package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingInvoiceRequest {

    private Long patientId;
    private Long admittedPatientId;
    private Long referringDoctorId;
    private String invoiceType; // INPATIENT, OUTPATIENT, EMERGENCY
    private Double taxRate;
    private Double discountPercent;
    private String notes;
    private String preparedBy;
    private List<BillingInvoiceItemRequest> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BillingInvoiceItemRequest {
        private Long chargeCategoryId;
        private String categoryCode;
        private String description;
        private Integer quantity;
        private Double unitPrice;
        private Double discountPercent;
        private String sourceModule;
        private Long sourceId;
    }
}
