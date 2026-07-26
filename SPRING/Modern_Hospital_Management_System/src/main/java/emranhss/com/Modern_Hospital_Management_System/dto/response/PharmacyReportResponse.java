package emranhss.com.Modern_Hospital_Management_System.dto.response;
import lombok.Data;
import java.util.List;
import java.util.Map;
@Data
public class PharmacyReportResponse {
    private String reportType;
    private String startDate;
    private String endDate;
    private Double totalSales;
    private Double totalPurchases;
    private Double totalProfit;
    private Long totalTransactions;
    private List<Map<String, Object>> data;
}
