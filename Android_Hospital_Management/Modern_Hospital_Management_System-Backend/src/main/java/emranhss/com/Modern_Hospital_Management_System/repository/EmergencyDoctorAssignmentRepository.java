package emranhss.com.Modern_Hospital_Management_System.repository;

import emranhss.com.Modern_Hospital_Management_System.entity.EmergencyDoctorAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyDoctorAssignmentRepository extends JpaRepository<EmergencyDoctorAssignment, Long> {
    List<EmergencyDoctorAssignment> findByEmergencyPatientId(Long emergencyPatientId);
    List<EmergencyDoctorAssignment> findByDoctorId(Long doctorId);
    List<EmergencyDoctorAssignment> findByIsActive(Boolean isActive);
    long countByIsActive(Boolean isActive);
}
