package emranhss.com.Modern_Hospital_Management_System.dto.response;
import lombok.Data;
import java.util.List;
import java.util.Map;
@Data
public class PharmacyDashboardResponse {
    private Double todaySales;
    private Long todaySalesCount;
    private Double todayPurchases;
    private Long todayPurchasesCount;
    private Long totalMedicines;
    private Long totalAvailableStock;
    private Long lowStockCount;
    private Long expiredCount;
    private Long expiringSoonCount;
    private Double monthlyRevenue;
    private Double monthlyProfit;
    private List<PharmacySaleResponse> recentSales;
    private List<Map<String, Object>> topSellingMedicines;
    private List<Map<String, Object>> salesChart;
    private List<Map<String, Object>> expiryAlerts;
}
