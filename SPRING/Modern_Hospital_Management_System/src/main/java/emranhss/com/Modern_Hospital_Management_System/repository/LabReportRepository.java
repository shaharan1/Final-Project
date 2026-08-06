package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.LabReport;
import emranhss.com.Modern_Hospital_Management_System.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LabReportRepository extends JpaRepository<LabReport, Long> {
    Optional<LabReport> findByTestOrderId(Long testOrderId);
    List<LabReport> findByTestOrderPatientIdOrderByCreatedDateDesc(Long patientId);
    List<LabReport> findAllByOrderByCreatedDateDesc();
    long countByReportStatus(ReportStatus reportStatus);
    long countByReportStatusNot(ReportStatus reportStatus);
}
