package emranhss.com.Modern_Hospital_Management_System.service;

import java.util.List;
import java.util.Map;

public interface BillingDashboardService {

    Map<String, Object> getDashboardSummary();

    List<Map<String, Object>> getDailyRevenueChart();

    List<Map<String, Object>> getMonthlyRevenueChart();

    List<Map<String, Object>> getDepartmentRevenue();

    List<Map<String, Object>> getPaymentMethodDistribution();

    List<Map<String, Object>> getRecentActivity();
}
