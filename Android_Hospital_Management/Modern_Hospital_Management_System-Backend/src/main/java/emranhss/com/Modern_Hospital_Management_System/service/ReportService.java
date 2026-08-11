package emranhss.com.Modern_Hospital_Management_System.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportService {

    Map<String, Object> getDashboardSummary();

    Map<String, Object> getPatientAnalytics();

    Map<String, Object> getAppointmentAnalytics();

    Map<String, Object> getDoctorAnalytics();

    Map<String, Object> getLabAnalytics();

    Map<String, Object> getPharmacyAnalytics();

    Map<String, Object> getRevenueAnalytics();

    Map<String, Object> getBedOccupancy();

    Map<String, Object> getEmergencyAnalytics();

    Map<String, Object> getFinancialAnalytics();

    Map<String, Object> getDailyRevenue(LocalDate date);

    Map<String, Object> getMonthlyRevenue(int year, int month);

    List<Map<String, Object>> getDepartmentRevenue();

    List<Map<String, Object>> getTopDoctors();

    List<Map<String, Object>> getTopMedicines();

    List<Map<String, Object>> getRecentActivity();
}
