package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.SurgeryMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SurgeryDashboardResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SurgeryResponse;
import emranhss.com.Modern_Hospital_Management_System.repository.SurgeryRepository;
import emranhss.com.Modern_Hospital_Management_System.service.SurgeryDashboardService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurgeryDashboardServiceImp implements SurgeryDashboardService {

    private final SurgeryRepository surgeryRepository;
    private final SurgeryMapper surgeryMapper;

    public SurgeryDashboardServiceImp(SurgeryRepository surgeryRepository, SurgeryMapper surgeryMapper) {
        this.surgeryRepository = surgeryRepository;
        this.surgeryMapper = surgeryMapper;
    }

    @Override
    public SurgeryDashboardResponse getDashboardSummary() {
        return getDashboardSummary(LocalDate.now());
    }

    @Override
    public SurgeryDashboardResponse getDashboardSummary(LocalDate date) {
        LocalDate day = date != null ? date : LocalDate.now();
        SurgeryDashboardResponse response = new SurgeryDashboardResponse();

        response.setReportDate(day);
        response.setTotalSurgeriesToday(surgeryRepository.countBySurgeryDate(day));
        response.setScheduledSurgeries(surgeryRepository.countByStatusAndSurgeryDate("SCHEDULED", day));
        response.setInProgressSurgeries(surgeryRepository.countByStatusAndSurgeryDate("IN_PROGRESS", day));
        response.setCompletedSurgeries(surgeryRepository.countByStatus("COMPLETED"));
        response.setCancelledSurgeries(surgeryRepository.countByStatus("CANCELLED"));

        long totalScheduled = response.getScheduledSurgeries() + response.getInProgressSurgeries();
        double utilization = 0.0;
        if (totalScheduled > 0) {
            long otSlots = Math.max(1, surgeryRepository.countBySurgeryDate(day));
            utilization = Math.min(100.0, Math.round((totalScheduled * 100.0) / otSlots));
        }
        response.setOtUtilizationPercent(utilization);

        response.setTotalSurgeryRevenue(
                surgeryRepository.topSurgeries().stream()
                        .mapToDouble(row -> row[2] != null ? ((Number) row[2]).doubleValue() : 0.0)
                        .sum());

        long pendingBills = surgeryRepository.findByStatusOrderBySurgeryDateDesc("COMPLETED").stream()
                .filter(s -> s.getBillingInvoiceId() == null)
                .count();
        response.setPendingSurgeryBills(pendingBills);
        response.setPendingBillAmount(
                surgeryRepository.findByStatusOrderBySurgeryDateDesc("COMPLETED").stream()
                        .filter(s -> s.getBillingInvoiceId() == null)
                        .mapToDouble(s -> s.getFinalPayable() != null ? s.getFinalPayable() : 0.0)
                        .sum());

        response.setTopSurgeons(surgeryRepository.topSurgeons());
        response.setTopCategories(surgeryRepository.topCategories());
        response.setTopPerformedSurgeries(surgeryRepository.topSurgeries());
        response.setStatusBreakdown(surgeryRepository.countByStatusGroup());
        response.setMonthlyStats(surgeryRepository.monthlyStats(day.getYear()));

        List<SurgeryResponse> upcoming = surgeryRepository
                .findBySurgeryDateBetweenOrderByStartTimeAsc(day, day.plusDays(7)).stream()
                .filter(s -> !"CANCELLED".equals(s.getStatus()))
                .map(surgeryMapper::toResponse)
                .collect(Collectors.toList());
        response.setUpcomingOtSchedule(upcoming.size() > 10 ? upcoming.subList(0, 10) : upcoming);

        List<SurgeryResponse> recent = surgeryRepository.findAllActiveOrderByDateDesc().stream()
                .limit(8)
                .map(surgeryMapper::toResponse)
                .collect(Collectors.toList());
        response.setRecentActivities(recent);

        return response;
    }
}
