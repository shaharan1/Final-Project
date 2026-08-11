package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.response.SurgeryDashboardResponse;

import java.time.LocalDate;

public interface SurgeryDashboardService {

    SurgeryDashboardResponse getDashboardSummary();

    SurgeryDashboardResponse getDashboardSummary(LocalDate date);
}
