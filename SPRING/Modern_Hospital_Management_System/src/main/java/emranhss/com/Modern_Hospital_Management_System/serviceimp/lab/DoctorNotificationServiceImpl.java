package emranhss.com.Modern_Hospital_Management_System.serviceimp.lab;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.LabReportMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.response.DoctorNotificationResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Doctor;
import emranhss.com.Modern_Hospital_Management_System.entity.DoctorNotification;
import emranhss.com.Modern_Hospital_Management_System.entity.LabReport;
import emranhss.com.Modern_Hospital_Management_System.entity.Tests;
import emranhss.com.Modern_Hospital_Management_System.enums.ReportStatus;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.DoctorNotificationRepository;
import emranhss.com.Modern_Hospital_Management_System.service.lab.DoctorNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorNotificationServiceImpl implements DoctorNotificationService {

    private final DoctorNotificationRepository doctorNotificationRepository;

    @Override
    @Transactional
    public DoctorNotificationResponse notifyReportReady(LabReport report) {
        if (report == null || report.getTestOrder() == null) return null;
        Tests test = report.getTestOrder();
        Doctor doctor = test.getPrescribedBy();
        if (doctor == null) return null;

        DoctorNotification notification = new DoctorNotification();
        notification.setDoctor(doctor);
        notification.setPatient(test.getPatient());
        notification.setTestOrderId(test.getId());

        ReportStatus status = report.getReportStatus();
        boolean critical = status == ReportStatus.CRITICAL || status == ReportStatus.DENGUE_POSITIVE;

        notification.setTitle(critical ? "CRITICAL Lab Result" : "Lab Report Ready");
        notification.setMessage("Lab report " + report.getReportNumber() + " for "
                + (test.getPatient() != null ? test.getPatient().getName() : "patient")
                + " is ready. Status: " + status + ". "
                + (report.getFinalImpression() != null ? report.getFinalImpression() : ""));
        notification.setType("LAB_REPORT_READY");
        notification.setSeverity(critical ? "CRITICAL" : "INFO");
        notification.setIsRead(false);

        DoctorNotification saved = doctorNotificationRepository.save(notification);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorNotificationResponse> getByDoctor(Long doctorId) {
        return doctorNotificationRepository.findByDoctorIdOrderByCreatedDateDesc(doctorId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorNotificationResponse> getUnreadByDoctor(Long doctorId) {
        return doctorNotificationRepository.findByDoctorIdAndIsReadFalseOrderByCreatedDateDesc(doctorId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount(Long doctorId) {
        return doctorNotificationRepository.countByDoctorIdAndIsReadFalse(doctorId);
    }

    @Override
    @Transactional
    public void markRead(Long id) {
        DoctorNotification notification = doctorNotificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + id));
        notification.setIsRead(true);
        doctorNotificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllRead(Long doctorId) {
        List<DoctorNotification> unread = doctorNotificationRepository.findByDoctorIdAndIsReadFalseOrderByCreatedDateDesc(doctorId);
        unread.forEach(n -> n.setIsRead(true));
        doctorNotificationRepository.saveAll(unread);
    }

    private DoctorNotificationResponse toResponse(DoctorNotification n) {
        DoctorNotificationResponse resp = new DoctorNotificationResponse();
        resp.setId(n.getId());
        resp.setDoctorId(n.getDoctor() != null ? n.getDoctor().getId() : null);
        resp.setPatientId(n.getPatient() != null ? n.getPatient().getId() : null);
        resp.setPatientName(n.getPatient() != null ? n.getPatient().getName() : null);
        resp.setPatientCode(n.getPatient() != null ? n.getPatient().getPatientCode() : null);
        resp.setTestOrderId(n.getTestOrderId());
        resp.setTitle(n.getTitle());
        resp.setMessage(n.getMessage());
        resp.setType(n.getType());
        resp.setSeverity(n.getSeverity());
        resp.setIsRead(n.getIsRead());
        resp.setCreatedDate(n.getCreatedDate());
        return resp;
    }
}
