package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.LabReportResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabReportResultRepository extends JpaRepository<LabReportResult, Long> {
    List<LabReportResult> findByLabReportIdOrderByDisplayOrderAsc(Long labReportId);
    void deleteByLabReportId(Long labReportId);
}
