package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.DoctorNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorNotificationRepository extends JpaRepository<DoctorNotification, Long> {
    List<DoctorNotification> findByDoctorIdOrderByCreatedDateDesc(Long doctorId);
    List<DoctorNotification> findByDoctorIdAndIsReadFalseOrderByCreatedDateDesc(Long doctorId);
    long countByDoctorIdAndIsReadFalse(Long doctorId);
}
