package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurgeryDashboardResponse {

    private long totalSurgeriesToday;
    private long scheduledSurgeries;
    private long completedSurgeries;
    private long cancelledSurgeries;
    private long inProgressSurgeries;
    private double otUtilizationPercent;
    private double totalSurgeryRevenue;
    private long pendingSurgeryBills;
    private double pendingBillAmount;

    private List<Object[]> topSurgeons = new ArrayList<>();
    private List<Object[]> topCategories = new ArrayList<>();
    private List<Object[]> topPerformedSurgeries = new ArrayList<>();
    private List<SurgeryResponse> upcomingOtSchedule = new ArrayList<>();
    private List<SurgeryResponse> recentActivities = new ArrayList<>();
    private List<Object[]> monthlyStats = new ArrayList<>();
    private List<Object[]> statusBreakdown = new ArrayList<>();

    private LocalDate reportDate = LocalDate.now();
}
