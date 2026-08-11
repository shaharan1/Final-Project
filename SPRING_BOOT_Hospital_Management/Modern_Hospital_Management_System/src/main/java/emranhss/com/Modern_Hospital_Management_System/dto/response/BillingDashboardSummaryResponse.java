package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BillingDashboardSummaryResponse {

    private long totalInvoices;
    private long todayInvoices;
    private Double totalRevenue;
    private Double todayRevenue;
    private Double totalCollected;
    private Double totalDue;
    private long unpaidCount;
    private long partialCount;
    private long paidCount;

    private List<CategoryRevenue> revenueByCategory;
    private List<PaymentMethodSummary> paymentMethodBreakdown;
    private List<DailyRevenue> dailyRevenueChart;

    @Data
    public static class CategoryRevenue {
        private String categoryCode;
        private String categoryName;
        private Double totalAmount;
        private Integer itemCount;
    }

    @Data
    public static class PaymentMethodSummary {
        private String method;
        private long count;
        private Double totalAmount;
    }

    @Data
    public static class DailyRevenue {
        private String date;
        private Double revenue;
        private Double collected;
    }
}
