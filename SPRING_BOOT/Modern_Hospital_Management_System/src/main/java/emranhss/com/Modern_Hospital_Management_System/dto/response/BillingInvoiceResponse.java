package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BillingInvoiceResponse {

    private Long id;
    private String invoiceNumber;
    private Long patientId;
    private String patientName;
    private String patientCode;
    private String patientPhone;
    private Long admittedPatientId;
    private String admissionWard;
    private String admissionBed;
    private Long referringDoctorId;
    private String referringDoctorName;
    private String invoiceType;

    private List<BillingInvoiceItemResponse> items;

    private Double subtotal;
    private Double taxRate;
    private Double taxAmount;
    private Double discountPercent;
    private Double discountAmount;
    private Double netAmount;
    private Double totalPaid;
    private Double dueAmount;
    private String paymentStatus;
    private String invoiceStatus;
    private String notes;
    private String preparedBy;
    private String finalizedBy;
    private LocalDateTime finalizedDate;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdated;

    @Data
    public static class BillingInvoiceItemResponse {
        private Long id;
        private Long chargeCategoryId;
        private String categoryCode;
        private String categoryName;
        private String description;
        private Integer quantity;
        private Double unitPrice;
        private Double discountPercent;
        private Double discountAmount;
        private Double amount;
        private String sourceModule;
        private Long sourceId;
        private String itemStatus;
    }
}
