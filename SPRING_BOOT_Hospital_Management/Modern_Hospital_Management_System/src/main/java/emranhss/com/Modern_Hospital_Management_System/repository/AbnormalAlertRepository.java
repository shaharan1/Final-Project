package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.AbnormalAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbnormalAlertRepository extends JpaRepository<AbnormalAlert, Long> {
    List<AbnormalAlert> findByPatientIdOrderByCreatedDateDesc(Long patientId);
    List<AbnormalAlert> findByResolvedFalseOrderByCreatedDateDesc();
    long countByResolvedFalse();
}
