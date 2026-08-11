package emranhss.com.Modern_Hospital_Management_System.service.lab;

import emranhss.com.Modern_Hospital_Management_System.entity.LabReport;
import emranhss.com.Modern_Hospital_Management_System.dto.response.DoctorNotificationResponse;

import java.util.List;

public interface DoctorNotificationService {

    DoctorNotificationResponse notifyReportReady(LabReport report);

    List<DoctorNotificationResponse> getByDoctor(Long doctorId);

    List<DoctorNotificationResponse> getUnreadByDoctor(Long doctorId);

    long getUnreadCount(Long doctorId);

    void markRead(Long id);

    void markAllRead(Long doctorId);
}
